/*Implementing module without updating /proc/ksyms
Author:Team-c
Version:1.0
*/


#define MODULE
#define __KERNEL__


#include<linux/module.h>
#include<linux/version.h>
#include<linux/kernel.h>
#include<linux/init.h>

/*Kernel module Comments*/
MODULE_AUTHOR("TEAM VEDA");
MODULE_DESCRIPTION("FIRST MODULE");
MODULE_LICENSE("private");

EXPORT_NO_SYMBOLS;// map all symbols local

int val=300;

void func()
{
	printk("func invoked\n ");
	printk(" val = %d",val);
}	

int myinit(void)
{
	printk("module inserted\n ");
	return 0;
}

void myexit(void)
{
	printk("module removed\n ");
}

module_init(myinit);
module_exit(myexit);

