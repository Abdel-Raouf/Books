
# define REALTEK_VENDER_ID    0x10EC
# define REALTEK_DEVICE_ID    0x8139


#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/stddef.h>
#include <linux/pci.h>
#include <linux/netdevice.h>
#include <linux/etherdevice.h>
#include <linux/delay.h>

#include "rtl8139.h"


 irqreturn_t  rtl8139_interrupt (int irq, void *dev_instance, struct pt_regs *regs) {
        struct net_device *dev = (struct net_device*)dev_instance;
        struct rtl8139_private *tp = dev->priv;
        void *ioaddr = tp->mmio_addr;
        unsigned short isr = readw(ioaddr + ISR);
        
        /* clear all interrupt.
         * Specs says reading ISR clears all interrupts and writing
         * has no effect. But this does not seem to be case. I keep on
         * getting interrupt unless I forcibly clears all interrupt :-(
         */
        writew(0xffff, ioaddr + ISR);

        if((isr & TxOK) || (isr & TxErr)) 
        {
                while((tp->dirty_tx != tp->cur_tx) || netif_queue_stopped(dev))
                {
                        unsigned int txstatus = 
                                readl(ioaddr + TSD0 + tp->dirty_tx * sizeof(int));

                        if(!(txstatus & (TxStatOK | TxAborted | TxUnderrun)))
                                break; /* yet not transmitted */

                        if(txstatus & TxStatOK) {
                                printk("Transmit OK interrupt\n");
                                tp->stats.tx_bytes += (txstatus & 0x1fff);
                                tp->stats.tx_packets++;
                        }
                        else {
                                printk("Transmit Error interrupt\n");
                                tp->stats.tx_errors++;
                        }
                                
                        tp->dirty_tx++;
                        tp->dirty_tx = tp->dirty_tx % NUM_TX_DESC;

                        if((tp->dirty_tx == tp->cur_tx) & netif_queue_stopped(dev))
                        {
                                printk("waking up queue\n");
                                netif_wake_queue(dev);
                        }
                }
        }

        if(isr & RxErr) {
                /* TODO: Need detailed analysis of error status */
                printk("receive err interrupt\n");
                tp->stats.rx_errors++;
        }

        if(isr & RxOK) {
                printk("receive interrupt received\n");
                while((readb(ioaddr + CR) & RxBufEmpty) == 0)
                {
                        unsigned int rx_status;
                        unsigned short rx_size;
                        unsigned short pkt_size;
                        struct sk_buff *skb;

                        if(tp->cur_rx > RX_BUF_LEN)
                                tp->cur_rx = tp->cur_rx % RX_BUF_LEN;
        
                        /* TODO: need to convert rx_status from little to host endian
                         * XXX: My CPU is little endian only :-)
                         */
                        rx_status = *(unsigned int*)(tp->rx_ring + tp->cur_rx);
                        rx_size = rx_status >> 16;
                        
                        /* first two bytes are receive status register 
                         * and next two bytes are frame length
                         */
                        pkt_size = rx_size - 4;

                        /* hand over packet to system */
                        skb = dev_alloc_skb (pkt_size + 2);
                        if (skb) {
                                skb->dev = dev;
                                skb_reserve (skb, 2); /* 16 byte align the IP fields */

                                eth_copy_and_sum(
                                        skb, tp->rx_ring + tp->cur_rx + 4, pkt_size, 0);

                                skb_put (skb, pkt_size);
                                skb->protocol = eth_type_trans (skb, dev);
                                netif_rx (skb);

                                dev->last_rx = jiffies;
                                tp->stats.rx_bytes += pkt_size;
                                tp->stats.rx_packets++;
                        } 
                        else {
                                printk("Memory squeeze, dropping packet.\n");
                                tp->stats.rx_dropped++;
                        }
                
                        /* update tp->cur_rx to next writing location  */
                        tp->cur_rx = (tp->cur_rx + rx_size + 4 + 3) & ~3;

			/* Update CAPR */
                        writew(tp->cur_rx, ioaddr + CAPR);
                }
        }
        
        if(isr & CableLen)
                printk("cable length change interrupt\n");
        if(isr & TimeOut)
                printk("time interrupt\n");
        if(isr & SysErr)
                printk("system err interrupt\n");
        return 0 ;
}



static int rtl8139_open(struct net_device *dev) {

        struct rtl8139_private *tp = dev->priv;
        int retval;

        /* get the IRQ
         * second arg is interrupt handler
         * third is flags, 0 means no IRQ sharing
         */

        retval = request_irq(dev->irq, rtl8139_interrupt, 0, dev->name, dev);
        if(retval)
                return retval;

        /* get memory for Tx buffers
         * memory must be DMAable
         */
        tp->tx_bufs = pci_alloc_consistent(
                        tp->pci_dev, TOTAL_TX_BUF_SIZE, &tp->tx_bufs_dma);
        
        if(!tp->tx_bufs) {
                free_irq(dev->irq, dev);
                return -ENOMEM;
        }

        tp->tx_flag = 0;
        rtl8139_init_ring(dev);
        rtl8139_hw_start(dev);

	tp->rx_ring = pci_alloc_consistent(
        tp->pci_dev, RX_BUF_TOT_LEN, &tp->rx_ring_dma);

if((!tp->tx_bufs)  || (!tp->rx_ring)) {
        free_irq(dev->irq, dev);

        if(tp->tx_bufs) {
                pci_free_consistent(tp->pci_dev, 
TOTAL_TX_BUF_SIZE, tp->tx_bufs, tp->tx_bufs_dma);
                        tp->tx_bufs = NULL;
                }
        if(tp->rx_ring) {
                pci_free_consistent(tp->pci_dev, 
RX_BUF_TOT_LEN, tp->rx_ring, tp->rx_ring_dma);
                        tp->rx_ring = NULL;
                }
        return -ENOMEM;
}
        return 0;
}

void rtl8139_init_ring (struct net_device *dev)
{
        struct rtl8139_private *tp = dev->priv;
        int i;

        tp->cur_tx = 0;
        tp->dirty_tx = 0;

        for (i = 0; i < NUM_TX_DESC; i++)
                tp->tx_buf[i] = &tp->tx_bufs[i * TX_BUF_SIZE];
        
}

void rtl8139_hw_start (struct net_device *dev)
{
        struct rtl8139_private *tp = dev->priv;
        void *ioaddr = tp->mmio_addr;
        u32 i;

        rtl8139_chip_reset(ioaddr);

        /* Must enable Tx/Rx before setting transfer thresholds! */
        writeb(CmdTxEnb | CmdRxEnb, ioaddr + CR);

        /* tx config */
        writel(0x00000600, ioaddr + TCR); /* DMA burst size 1024 */

        /* rx config */
        writel(((1 << 12) | (7 << 8) | (1 << 7) | 
                                (1 << 3) | (1 << 2) | (1 << 1)), ioaddr + RCR);

        /* init Tx buffer DMA addresses */
        for (i = 0; i < NUM_TX_DESC; i++) {
                writel(tp->tx_bufs_dma + (tp->tx_buf[i] - tp->tx_bufs),
                                        ioaddr + TSAD0 + (i * 4));
        }

        /* init RBSTART */
        writel(tp->rx_ring_dma, ioaddr + RBSTART);

        /* initialize missed packet counter */
        writel(0, ioaddr + MPC);

        /* no early-rx interrupts */
        writew((readw(ioaddr + MULINT) & 0xF000), ioaddr + MULINT);

        /* Enable all known interrupts by setting the interrupt mask. */
        writew(INT_MASK, ioaddr + IMR);

        netif_start_queue (dev);
}



void rtl8139_chip_reset (void *ioaddr)
{
        int i;

        /* Soft reset the chip. */
        writeb(CmdReset, ioaddr + CR);

        /* Check that the chip has finished the reset. */
        for (i = 1000; i > 0; i--) {
                barrier();
                if ((readb(ioaddr + CR) & CmdReset) == 0)
                        break;
                udelay(10);
        }
}


static int rtl8139_stop(struct net_device *dev) {
printk("rtl8139_stop is called \n");
return 0;
}

static int rtl8139_start_xmit(struct sk_buff *skb, struct net_device *dev)
{
        struct rtl8139_private *tp = dev->priv;
        void *ioaddr = tp->mmio_addr;
        unsigned int entry = tp->cur_tx;
        unsigned int len = skb->len;
#define ETH_MIN_LEN 60  /* minimum Ethernet frame size */
        if (len < TX_BUF_SIZE) {
                if(len < ETH_MIN_LEN)
                        memset(tp->tx_buf[entry], 0, ETH_MIN_LEN);
                skb_copy_and_csum_dev(skb, tp->tx_buf[entry]);
                dev_kfree_skb(skb);
        } else {
                dev_kfree_skb(skb);
                return 0;
        }
        writel(tp->tx_flag | max(len, (unsigned int)ETH_MIN_LEN), 
                        ioaddr + TSD0 + (entry * sizeof (u32)));
        entry++;
        tp->cur_tx = entry % NUM_TX_DESC;

        if(tp->cur_tx == tp->dirty_tx) {
                netif_stop_queue(dev);
        }
        return 0;
}
static int cnt;           
static struct net_device_stats* rtl8139_get_stats(struct net_device *dev) {
 struct rtl8139_private *tp = dev->priv;
	
printk("\n rtl8139_get_stats %d",cnt);	
cnt++;
  return &(tp->stats);
}

static int rtl8139_init(struct pci_dev *pdev, struct net_device **dev_out) 
{
        struct net_device *dev=NULL;
        struct rtl8139_private *tp=NULL;
        /* 
         * alloc_etherdev allocates memory for dev and dev->priv.
         * dev->priv shall have sizeof(struct rtl8139_private) memory
         * allocated.
         */
        dev = alloc_etherdev(sizeof(*tp));

        if(!dev) {
                printk("Could not allocate etherdev\n");
                return -1;
        }

        tp = dev->priv;
        tp->pci_dev = pdev;
        *dev_out = dev;

        return 0;
}
                                                
int init_module(void) {
    struct pci_dev *pdev;
    unsigned long mmio_start, mmio_end, mmio_len, mmio_flags;
    void *ioaddr;
    struct rtl8139_private *tp;
    int i;


    pdev = pci_find_device(REALTEK_VENDER_ID, REALTEK_DEVICE_ID, NULL);

    if(!pdev)
    {	
	printk("Device not Found ");
    }
    else                                 // Device Found, Enable it.
    {	
        printk("Device Found");
	 if(pci_enable_device(pdev)) {
                        printk("Could not enable the device\n");
                        return -1;
                }

	       if(rtl8139_init(pdev, &rtl8139_dev)) {
                printk("Could not initialize device\n");
                return 0;
        }
		  tp = rtl8139_dev->priv; /* rtl8139 private information */

		      /* get PCI memory mapped I/O space base address from BAR1 */
        mmio_start = pci_resource_start(pdev, 1);
        mmio_end = pci_resource_end(pdev, 1);
        mmio_len = pci_resource_len(pdev, 1);
        mmio_flags = pci_resource_flags(pdev, 1);

	   /* make sure above region is MMIO */
        if(!(mmio_flags & IORESOURCE_MEM)) {
                printk("region not MMIO region\n");
                return -1;
        }

	      /* get PCI memory space */
        if(pci_request_regions(pdev, DRIVER)) {
                printk("Could not get PCI region\n");
                return -1;
        }

	pci_set_master(pdev);

        /* ioremap MMIO region */
        ioaddr = ioremap(mmio_start, mmio_len);
        if(!ioaddr) {
                printk("Could not ioremap\n");
                return -1;
        }
	 rtl8139_dev->base_addr = (long)ioaddr;
        tp->mmio_addr = ioaddr;
        tp->regs_len = mmio_len;

        /* UPDATE NET_DEVICE */

        for(i = 0; i < 6; i++) {  /* Hardware Address */
                rtl8139_dev->dev_addr[i] = readb(rtl8139_dev->base_addr+i);
                rtl8139_dev->broadcast[i] = 0xff;
        }
        rtl8139_dev->hard_header_len = 14;

        memcpy(rtl8139_dev->name, "eth0", sizeof("eth0")); /* Device Name */
        rtl8139_dev->irq = pdev->irq;  /* Interrupt Number */
        rtl8139_dev->open = rtl8139_open;
        rtl8139_dev->stop = rtl8139_stop;
        rtl8139_dev->hard_start_xmit = rtl8139_start_xmit;
        rtl8139_dev->get_stats = rtl8139_get_stats;

/* register the device */
        if(register_netdev(rtl8139_dev)) {
                printk("Could not register netdevice\n");
                return -1;
        }

    }
    return 0;
}

void cleanup_module()
{
	struct rtl8139_private *tp;
        tp = rtl8139_dev->priv;

        iounmap(tp->mmio_addr);
        pci_release_regions(tp->pci_dev);

        unregister_netdev(rtl8139_dev);
        pci_disable_device(tp->pci_dev);
	printk("\n Module Unloaded Successfully");
}

