/* while_delay.c
** Author : Veda
** Version : 1.0
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
	unsigned long j = jiffies + 10*HZ;
	printk("Try to do something for the next 10 seconds.\n");
	printk("I am putting the system off to a standstill for the\n"
	       "next 10 seconds");
	while( jiffies < j ) { 
		
                }
	printk("Now I am out of the loop");
	return -1; /* dont load the module */
}

/* exit point */
static __exit void cleanup_module(void)
{

}


MODULE_AUTHOR("VEDA");
MODULE_DESCRIPTION("Example");
MODULE_LICENSE("GPL");
