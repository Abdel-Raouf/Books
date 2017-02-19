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

int myinit(void);
void myexit(void);

/* entry point */
int myinit(void)
{
	unsigned long low, high;
	printk("\n Module Inserted\n");
	rdtsc(low, high);
	printk("Low: %ld, High: %ld\n", low, high);
	return SUCCESS;
}

/* exit point */
void myexit(void)
{
	unsigned long low, high;
	
	rdtsc(low, high);
	printk("Low: %ld, High: %ld\n", low, high);
	printk("\n modle Removed\n");
}


MODULE_AUTHOR("VEDA");
MODULE_DESCRIPTION("Example");
MODULE_LICENSE("GPL");
module_init(myinit);
module_exit(myexit);
