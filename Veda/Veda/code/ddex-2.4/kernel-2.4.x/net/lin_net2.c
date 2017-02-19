/* Network Interface 1 - source code 
Author:Team-c
Version:1.0
*/

/*
The network interface will be called lin2
Hardware Address of this will be: "\0lin01"
*/

#ifndef __KERNEL__
	#define __KERNEL__
#endif

#ifndef MODULE
	#define MODULE
#endif

#define EXPORT_SYMTAB

/* includes - All network device drivers have to include these */
#include <linux/config.h>
#include <linux/module.h>

#include <linux/sched.h>
#include <linux/kernel.h> /* printk() */
#include <linux/slab.h> /* kmalloc() */
#include <linux/errno.h>  /* error codes */
#include <linux/types.h>  /* size_t */
#include <linux/interrupt.h> /* mark_bh */
#include <asm/uaccess.h>
#include <linux/sched.h>

#include <linux/in.h>
#include <linux/netdevice.h>   /* struct device, and other headers */
#include <linux/etherdevice.h> /* eth_type_trans */
#include <linux/ip.h>          /* struct iphdr */
#include <linux/tcp.h>         /* struct tcphdr */
#include <linux/skbuff.h>

#ifdef LINUX_20
#  include <linux/if_ether.h>
#  define net_device_stats enet_statistics
#else
#  include <linux/in6.h>
#endif
#include <asm/checksum.h>
#include <asm/byteorder.h>

#ifdef htons
#undef htons
#endif

#define htons(x) __cpu_to_be16(x) /* do nothing */


/* We are back with our section */
#include "lin_net_device.h"
#define lin_NET2_TIMEOUT	5 /*jiffies*/
#define SUCCESS			0

static __init int  lin_net2_module_init(void);

static __exit void lin_net2_module_exit(void);

static __exit int  lin_net2_init(struct net_device *dev);

static __exit int  lin_net2_open(struct net_device *dev);

static __exit int  lin_net2_stop(struct net_device *dev);

static __exit int  lin_net2_set_config(struct net_device *dev,
				struct ifmap *map);
				
static __exit int  lin_net2_hard_start_xmit(struct sk_buff *skb, 
				struct net_device *dev);
				
static __exit int  lin_net2_do_ioctl(struct net_device *dev,
				struct ifreq *ifr, int cmd);
				
static __exit struct net_device_stats* lin_net2_get_stats(
				struct net_device *dev);
				
static __exit int  lin_net2_change_mtu(struct net_device *dev, 
				int new_mtu);
				
static __exit int  lin_net2_rebuild_header(struct sk_buff *skb);

static __exit int  lin_net2_hard_header(struct sk_buff *skb,
				struct net_device *dev,
				unsigned short type,
				void *daddr,
				void *saddr,
				unsigned len);			
					
static __exit void  lin_net2_tx_timeout(struct net_device *dev);

static __exit int   lin_net2_hw_tx(char *data, int len,
				struct net_device *dev);
				
static __exit void  lin_net2_interrupt(int irq, void *dev_id,
				struct pt_regs *regs);
				
module_init(lin_net2_module_init);
module_exit(lin_net2_module_exit);

struct net_device lin_net2_dev;

/* Declare our private data structure */
typedef struct __lin_net2_priv {
	struct net_device_stats stats;
	int status;
	int rx_packetlen;
	u8 *rx_packetdata;
	int tx_packetlen;
	u8 *tx_packetdata;
	struct sk_buff *skb;
} lin_net2_priv_t;
#define PRIV(X)  ((lin_net2_priv_t *)(X)->priv)
#define STATS(X) (PRIV((X))->stats)

/* Spin lock used for protecting critical code */
static spinlock_t lin_net2_lock;
static unsigned long lin_net2_flags;
#define lin_LOCK  	spin_lock_irqsave(&lin_net2_lock, lin_net2_flags)
#define lin_UNLOCK	spin_unlock_irqrestore(&lin_net2_lock, lin_net2_flags)

/* Pseudo Netdevice Descriptor */
static int lin_net2_d = -1;

/* Responsonility of lin_net2_module_init or init_module
   1. Copy the correct name on to lin_net2_dev.name.
   2. Register device using register_netdev which calls lin_net2_init
*/
static __init int  lin_net2_module_init(void)
{
	int error;
	/* step 1 */
	lin_net2_dev.init = lin_net2_init;
	strcpy(lin_net2_dev.name, "lin2");
	
	/* step 2 */
	if( (error = register_netdev(&lin_net2_dev)) ) {
		printk("Network device registration failure %d\n", error);
		return -ENODEV;
	}
	printk("lin_net2 loaded\n");
	return SUCCESS;
}

/* Responsibilities of this function
   1. Free the dev->priv field
   2. Call unregister_netdev function to unregister network device 
*/
static __exit void lin_net2_module_exit(void)
{
	/* step 1 */
	kfree(lin_net2_dev.priv);
	/* step 2 */
	unregister_netdev( &lin_net2_dev );
	printk("lin_net2 unloaded\n");
}

/* Responsibilities of device initialization function
   1. Call ether_setup to setup the device structure
   2. Register all the interface functions and other paramters
   3. Set this module as the owner of device *dev.
   4. Allocate space for lin_net2_priv_t datatype
   5. Initialize our spin lock lin_net2_lock
*/
static __exit int  lin_net2_init(struct net_device *dev)
{
	/* step 1 */
	ether_setup( dev );
	
	/* step 2 */
	dev->open		= lin_net2_open;
	dev->stop		= lin_net2_stop;
	dev->set_config		= lin_net2_set_config;
	dev->hard_start_xmit	= lin_net2_hard_start_xmit;
	dev->do_ioctl		= lin_net2_do_ioctl;
	dev->get_stats		= lin_net2_get_stats;
	dev->rebuild_header	= lin_net2_rebuild_header;
	dev->hard_header	= lin_net2_hard_header;
	dev->tx_timeout		= lin_net2_tx_timeout;
	dev->watchdog_timeo	= lin_NET2_TIMEOUT;
	dev->flags		|= IFF_NOARP;
	dev->hard_header_cache	= NULL;
	
	/* step 3 */
	SET_MODULE_OWNER(dev);
	
	/* step 4 */
	dev->priv = kmalloc( sizeof(lin_net2_priv_t), GFP_KERNEL );
	if( dev->priv == NULL )
		return -ENOMEM;
	memset( dev->priv, 0, sizeof(lin_net2_priv_t) );
	
	/* step 5 */
	spin_lock_init( &lin_net2_lock );
	return 0;
}

/* We must know that this function is called when ifconfig net1 up 
   is executed. So the responsibilites of this function are
   1. Increment module usage counter
   2. assign hardware address "\0lin01"
   3. Only in this case register pseudo hardware device
   4. start device queue
*/
static __exit int  lin_net2_open(struct net_device *dev)
{
	/* step 1 */
	MOD_INC_USE_COUNT;
	/* step 2 */
	memcpy(dev->dev_addr, "\0lin01", ETH_ALEN);
	/* step 3 */
	lin_net2_d = register_pseudo_net_device(lin_net2_interrupt, dev );
	if( lin_net2_d < 0 ) 
		return -ENODEV;	
	/* step 4 */
	netif_start_queue(dev);
	return SUCCESS;
}

/* We must know that this function is called when ifconfig net1 down
   is executed. So the responsibilities of this function are
   1. Unregister our device
   2. Stop device queue
   3. Decrement the module usage counter
*/
static __exit int  lin_net2_stop(struct net_device *dev)
{
	/* step 1 */
	unregister_pseudo_net_device(lin_net2_d);
	/* step 2 */
	netif_stop_queue(dev);
	/* step 3 */
	MOD_DEC_USE_COUNT;
}

/* We must know that these config changes are made using ifconfig
   command. But the driver should ensure that changes are not made
   when the interface is up.
   1. Check if the interface is up. Return -EBUSY if so.
   2. Dont allow I/O address change
   3. Change IRQ if asked
   4. Ignore others
*/
static __exit int  lin_net2_set_config(struct net_device *dev,
				struct ifmap *map)
{
	/* Step 1 */
	if( dev->flags & IFF_UP )
		return -EBUSY;

	/* step 2 */
	if( map->base_addr != dev->base_addr ) {
		printk("lin1: Cant change I/O Address base\n");
		return -EOPNOTSUPP;
	}
	
	/* step 3 */
	if (map->irq != dev->irq)
		dev->irq = map->irq;

	/* step 4 */
	return SUCCESS;	
}

/* This function is called when transfer is to be initiated. 
   struct sk_buff *skb contains the packet with headers,
   data and other stuff.
   
   1. Resolve the length field
   2. Get a pointer to the data to be transmitted
   3. Save transmission time_stamp
   4. Do the actual transfer
   5. Free up the memory used by the socket buffer
*/
static __exit int  lin_net2_hard_start_xmit(struct sk_buff *skb, 
				struct net_device *dev)
{
	int len, retval; 
	char *data;
	
	lin_net2_priv_t *priv = (lin_net2_priv_t *)dev->priv;
	
	/* step 1 */
	if( skb->len < ETH_ZLEN )
		len = ETH_ZLEN;
	else
		len = skb->len;
	
	/* step 2 */
	data = skb->data;
	
	/* step 3 */
	dev->trans_start = jiffies;
	
	/* step 4 */
	retval = lin_net2_hw_tx(data, len, dev);
	
	/* step 5 */
	dev_kfree_skb(skb);
	
	return retval;
}

/* This is where the actual transmission of packet takes place.
   The data, len and dev is sent to us by lin_net2_hard_start_xmit
   
   First for some important data structures we will be seeing in
   this function.

<linux/if_ether.h>
struct ethhdr
{
	unsigned char   h_dest[ETH_ALEN];       // destination eth addr 
	unsigned char   h_source[ETH_ALEN];     // source ether addr    
	unsigned short  h_proto;                // packet type ID field 
}; 

sizeof(struct ethhdr) = 14 bytes

<linux/ip.h>
struct iphdr {
        __u8    ihl:4, version:4;
        __u8    tos;
        __u16   tot_len;
        __u16   id;
        __u16   frag_off;
        __u8    ttl;
        __u8    protocol;
        __u16   check;
        __u32   saddr;
        __u32   daddr;
};

sizeof(struct iphdr) = 20 bytes

Here __u8 is unsigned character byte. (8) 
     __u16 is unsigned word (16)
     __u32 is unsigned long word (32) 
     
char *data contains the data as it should appear on the physical
media. This ehternet packet contains 
        +------+--------+------------------------------+
	|ethhdr| iphdr  |       data in packet         +
	+------+--------+------------------------------+
	  14       20
	  
    Responsibilities
    1. Extract the destination ethernet address
    2. Extract the ip address and change the third octect
    3. Recalculate header checksum
    4. Write to hardware (defined in lin_net_device.c 
    5. Update statistics fields
*/
static __exit int   lin_net2_hw_tx(char *data, int len,
				struct net_device *dev)
{
	int retval;
	
	/* step 1 */
	struct ethhdr *ethh = (struct ethhdr*)data;
	char *dstaddr = ethh->h_dest;

	/* step 2 */
	u32 *saddr, *daddr;
	struct iphdr *ih = (struct iphdr*)(data+sizeof(struct ethhdr));
	
	saddr = &(ih->saddr);
	daddr = &(ih->daddr);

	((u8*)saddr)[2] ^= 1;
	((u8*)daddr)[2] ^= 1;

	/* step 3 */
	ih->check = 0;
	ih->check = ip_fast_csum((unsigned char *)ih,ih->ihl);
	
	/* step 4 */
	if( (retval = write_to_hardware(lin_net2_d, dstaddr, data, len)) < 0 ) {
		STATS(dev).tx_errors++;
		return retval;
	}
	
	/* step 5 */
	STATS(dev).tx_packets++;
	STATS(dev).tx_bytes += len;
	
	return SUCCESS;
}

/* This function returns 0 nothing else */
static __exit int  lin_net2_do_ioctl(struct net_device *dev,
				struct ifreq *ifr, int cmd)
{
	return 0;
}

/* Should return the statistics structure */
static __exit struct net_device_stats* lin_net2_get_stats(
				struct net_device *dev)
{
	return &STATS(dev);
}

/* This function is called when MTU has to be changed 
   Responsibilities
   1. Check the new mtu's limits
   2. LOCK - change mtu - UNLOCK
*/
static __exit int  lin_net2_change_mtu(struct net_device *dev, 
				int new_mtu)
{
	/* step 1 */
	if( (new_mtu < 68) || (new_mtu > 1500) )
		return -EINVAL;
		
	/* step 2 */
	lin_LOCK;
	dev->mtu = new_mtu;
	lin_UNLOCK;
	
	return SUCCESS;	
}

/* Rebuild header is called just before packet transmission
   The function makes use of ARP cache to fill the structure
   with missing information.
   This function is rerely used, hard_header is used more often
   
   1. Extract ethernet header from data
   2. Fillin the ethernet header char array
   
   We make a small assumption here that if lin_net2 has
   ethernet address "\0lin01" then
   lin_net1 has ethernet address "\0lin00"
*/
static __exit int  lin_net2_rebuild_header(struct sk_buff *skb)
{
	struct ethhdr *eth = (struct ethhdr*)skb;
	struct net_device *dev = skb->dev;
	memcpy(eth->h_source, dev->dev_addr, dev->addr_len);
	memcpy(eth->h_dest, dev->dev_addr, dev->addr_len);
	eth->h_dest[ETH_ALEN-1] = '0'; /*lin_net2 -> lin_net1*/
	return 0;
}

/* This function has to build the header from the scratch
   using the information that was previously provided to 
   it.
   
   daddr is null if hardware header was not previously known
   saddr is null is source hardware header was not previously known
   
   We have to copy "\0lin01" in h_source and
   we have to copy "\0lin00" in h_dest
*/
static __exit int  lin_net2_hard_header(struct sk_buff *skb,
	struct net_device *dev, unsigned short type, void *daddr,
	void *saddr, unsigned len)
{
	struct ethhdr *eth = (struct ethhdr *)skb_push(skb,ETH_HLEN);
	eth->h_proto = htons(type);
	memcpy(eth->h_source, dev->dev_addr, dev->addr_len);
	memcpy(eth->h_dest, dev->dev_addr, dev->addr_len);
	eth->h_dest[ETH_ALEN-1] = '0'; /*lin_net2 -> lin_net1*/
	return dev->hard_header_len;		
}
	
/* This function is called when the packet transmission is not
   complete even after the timeout period has ellapsed.
   It is not required in this example
   */
static __exit void  lin_net2_tx_timeout(struct net_device *dev)
{
	/* do nothing */
}

/* This function is called whenever an interrupt is raised
   by the hardware informing us about the data arrival
   at the hardware. Actually the hardware has to raise an interrupt
   to call this function, which is concurrent safe.
   
   In our example lin_net_device module calls it when another
   interface sends data to this device
   1. Call read_from_hardware and read data
   2. Create a new socket buffer
   3. Update statistics
   4. Pass on the socket buffer to upper layers
*/
static __exit void  lin_net2_interrupt(int irq, void *dev_id,
				struct pt_regs *regs)
{
	struct sk_buff *skb;
	struct net_device *dev = (struct net_device*)dev_id;
	char *data=0; int len;
	lin_net2_priv_t *priv = (lin_net2_priv_t*)dev->priv;
	if( !dev ) return;
	
	/* step 1 */
	if( read_from_hardware(lin_net2_d, &data, &len) < 0 )
		return;
	
	/* step 2 */
	skb = dev_alloc_skb(len+2);
	if( !skb ) {
		printk("lin_net2: Low on memory. Packet dropped\n");
		STATS(dev).rx_dropped++;
		return;
	}
	//skb_reserve(skb, 2);
	memcpy(skb_put(skb, len), data, len);
	skb->dev = dev;
	skb->protocol = eth_type_trans(skb, dev);
	skb->ip_summed = CHECKSUM_UNNECESSARY; /* don't check it */
	
	/* step 3 */
	STATS(dev).rx_packets++;
	STATS(dev).rx_bytes += len;
	
	/* step 4 */
	netif_rx(skb); /* pass it to upper layers */
}
/* End of code here */

MODULE_AUTHOR("Team Veda");
MODULE_DESCRIPTION("lin Net1 interface module");
MODULE_LICENSE("GPL");

