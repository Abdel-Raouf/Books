/*Shows the use of kernel JIFFIES
Author:Team-c
Version:1.0
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
#include <linux/init.h>
#include <linux/sched.h>

/* entry point */
static __init int init_module(void)
{
	unsigned long seconds_started, hours_started, min_started;
	printk("Jiffies Value: INIT: %u\n", jiffies);
	seconds_started = jiffies/100ul;
	min_started = seconds_started/60;
	hours_started = min_started/60;
	printk("Seconds = %u, Hours = %u, Minutes = %u\n", 
		seconds_started, hours_started, min_started);
	return SUCCESS;
}

/* exit point */
static __exit void cleanup_module(void)
{
	unsigned long seconds_started, hours_started, min_started;
	printk("Jiffies Value: CLEANUP: %u\n", jiffies);
	seconds_started = jiffies/100ul;
	min_started = seconds_started/60;
	hours_started = min_started/60;
	printk("Seconds = %u, Hours = %u, Minutes = %u\n", 
		seconds_started, hours_started, min_started);
}


MODULE_AUTHOR("Team Veda");
MODULE_DESCRIPTION("Example on jiffies");
//MODULE_LICENSE("GPL");
