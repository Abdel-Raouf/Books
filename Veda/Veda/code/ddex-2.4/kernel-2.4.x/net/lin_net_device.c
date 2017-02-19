/* Pseudo network device driver 
Author:Team-c
Version:.1.0
Remarks:The job of this module is to pass on data packets from one
   module to another based on its ehternet address
*/
#ifndef __KERNEL__
	#define __KERNEL__
#endif

#ifndef MODULE
	#define MODULE
#endif

#define EXPORT_SYMTAB
#define SUCCESS 0

#include <linux/kernel.h>
#include <linux/module.h>
#include<linux/modversions.h>
#include <linux/init.h>
#include <linux/if_ether.h>
#include <linux/ip.h>
#include <linux/spinlock.h>
#include <linux/errno.h>
#include <linux/slab.h>
#include <asm/semaphore.h>
#include <linux/netdevice.h>
#include <linux/sched.h>
//#include "lin_net_device.h"

static spinlock_t pnd_lock;
#define lin_LOCK	spin_lock_irqsave(pnd_lock, flags)
#define lin_UNLOCK  	spin_unlock_irqrestore(pnd_lock, flags)


static __exitdata struct semaphore pnd_sem;

typedef void (pn_interrupt_fn)(int irq, void *dev_id,
				struct pt_regs *regs);

typedef struct __pseudo_net_device {
	u8	registered; /* true/false */
	struct  net_device *dev;
	pn_interrupt_fn *interrupt_fn;
	char 	*data;
	int   	len;	
} pseudo_net_device_t;
#define MAX_DEVICES	4 /* at most four devices */
pseudo_net_device_t pseudo_devices[MAX_DEVICES];

/* Registers a device and returns an index in the array */
static __exit int register_pseudo_net_device(pn_interrupt_fn *fn, 
			struct net_device *dev)
{
	int i;
	for(i=0; i<MAX_DEVICES; i++) 
		if( !pseudo_devices[i].registered ) {
			pseudo_devices[i].registered = 0xff;
			pseudo_devices[i].interrupt_fn = fn;
			pseudo_devices[i].dev = dev;
			return i; /* success */
		}
	return -ENODEV;
}
EXPORT_SYMBOL_NOVERS(register_pseudo_net_device);

/* unregisters a registered device */
static __exit int unregister_pseudo_net_device(int nd)
{
	if( nd > MAX_DEVICES )
		return -EINVAL;
	if( !pseudo_devices[nd].registered )
		return -EINVAL;
	memset( &pseudo_devices[nd], 0, 
		sizeof(pseudo_net_device_t) );	
	return SUCCESS;
}
EXPORT_SYMBOL_NOVERS(unregister_pseudo_net_device);

/* writes data to another device with ethernet address dev_addr */
static __exit int write_to_hardware(int nd, char *dev_addr, char *data, int len)
{
	int i;
	pseudo_net_device_t *src, *dst=0;

	if( nd > MAX_DEVICES )
		return -EINVAL;
	src = &pseudo_devices[nd];
	if( src->registered == 0 ) 
		return -ENODEV;
	
	/* Write data into source hw */
	src->data = (char *)kmalloc( sizeof(char)*len, GFP_KERNEL );
	memcpy(src->data, data, len);
	src->len = len;
	
	/*src->data = data;
	src->len = len;*/
	
	/* look for dest */
	for(i=0; i<MAX_DEVICES; i++) {
		if( pseudo_devices[i].dev != 0 ) {
			if( memcmp(pseudo_devices[i].dev->dev_addr, 
				dev_addr, ETH_ALEN)==0 ) {
				dst = &pseudo_devices[i];
				break;
			}
		}
	}
	if( dst == 0 ) {
		kfree( src->data );
		src->data = 0;
		src->len = 0;
		return -ENODEV;
	} 
	
	/* Transfer data */
	dst->data = (char *)kmalloc( sizeof(char)*(src->len), GFP_KERNEL );
	memcpy(dst->data, src->data, src->len);
	dst->len = src->len;
	
	/*dst->data = src->data;
	dst->len = src->len;*/

	/* Clear data in source hw */
	kfree(src->data);
	src->len = 0;
	
	/* call the interrupt function is destination */
	dst->interrupt_fn(0, dst->dev, NULL);
	return SUCCESS;
}
EXPORT_SYMBOL_NOVERS(write_to_hardware);

/* reads data from the device */
static __exit int read_from_hardware(int nd, char **data, int *len)
{
	pseudo_net_device_t *dev;
	if( nd > MAX_DEVICES )
		return -EINVAL;
	
	dev = &pseudo_devices[nd];
	if( dev->registered == 0 ) 
		return -EINVAL;	

	/* Transfer the pointers */
	*data = dev->data;
	*len = dev->len;
	
	
	/* clear hw buffer */
	dev->data = 0;
	dev->len = 0;
	
	return SUCCESS;
}
EXPORT_SYMBOL_NOVERS(read_from_hardware);

/* entry point */
static __init int init_module(void)
{
	init_MUTEX( &pnd_sem );
	memset( pseudo_devices, 0, 
		sizeof(pseudo_net_device_t)*MAX_DEVICES );
	printk("Pseudo Network Device Registered\n");
	spin_lock_init(&pnd_lock);
	return SUCCESS;
}

/* exit point */
static __exit void cleanup_module(void)
{
	int i;
	for(i=0; i<MAX_DEVICES; i++)
		if( pseudo_devices[i].data )
			kfree(pseudo_devices[i].data);
	printk("Pseudo Network Device Unregistered\n");
}

MODULE_AUTHOR("Team Veda");
MODULE_DESCRIPTION("lin Pseudo Net Device Module");
MODULE_LICENSE("GPL");
