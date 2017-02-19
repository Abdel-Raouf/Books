

# define REALTEK_VENDER_ID    0x10EC
# define REALTEK_DEVICE_ID    0x8139


// This perform dummy n/w operations

#define DUMMY    

#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/stddef.h>
#include <linux/pci.h>
#include <linux/netdevice.h>
#include <linux/etherdevice.h>

//#include <linux/etherdevice.h>

#include "rtl8139.h"

static int rtl8139_open(struct net_device *dev) {
printk("rtl8139_open is called\n");
return 0;
}

static int rtl8139_stop(struct net_device *dev) {
printk("rtl8139_stop is called \n");
return 0;
}

static int rtl8139_start_xmit(struct sk_buff *skb, struct net_device *dev) {
printk("rtl8139_start_xmit is called\n");
return 0;
}

static struct net_device_stats* rtl8139_get_stats(struct net_device *dev) {
printk("\n rtl8139_get_stats ");
return NULL;
}

static int rtl8139_init(struct pci_dev *pdev, struct net_device **dev_out) 
{
        struct net_device *dev=NULL;
        struct rtl8139_private *tp=NULL;
//	int size = sizeof(struct rtl8139_private);
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

        memcpy(rtl8139_dev->name, DRIVER, sizeof(DRIVER)); /* Device Name */
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
        return;
}

