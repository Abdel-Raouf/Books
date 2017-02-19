#ifndef _NET_DEVICE_EXTERNS_
#define _NET_DEVICE_EXTERNS_

#define MAX_DEVICES	2

struct net_device;

typedef void (pn_interrupt_fn)(int irq, void *dev_id,
				struct pt_regs *regs);

extern int register_pseudo_net_device(pn_interrupt_fn *fn, 
				struct net_device *dev);
extern int unregister_pseudo_net_device(int nd);
extern int write_to_hardware(int nd, char *dev_addr, char *data, int len);
extern int read_from_hardware(int nd, char **data, int *len);

unsigned long nj;

#define DY(X) \
	printk(X); \
	nj = jiffies+10*HZ; \
	while(jiffies<nj) { }
	
#endif
