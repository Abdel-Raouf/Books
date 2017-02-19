/*tsc.c
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
#include <asm/msr.h>

/* entry point */
static __init int init_module(void)
{
	unsigned long low, high;
	rdtsc(low, high);
	printk("Low: %u, High: %u\n", low, high);
	return SUCCESS;
}

/* exit point */
static __exit void cleanup_module(void)
{
	unsigned long low, high;
	rdtsc(low, high);
	printk("Low: %u, High: %u\n", low, high);
}


MODULE_AUTHOR("VEDA");
MODULE_DESCRIPTION("Example");
MODULE_LICENSE("GPL");
