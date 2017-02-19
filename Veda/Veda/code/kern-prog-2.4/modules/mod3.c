/*Module exporting choosen symbols
Author:Team-c
Version:1.0
*/


#define MODULE
#define __KERNEL__

//EXPORT_NO_SYMBOLS;

#include<linux/module.h>
#include<linux/modversions.h>
#include<linux/kernel.h>
#include<linux/init.h>

/*Kernel module Comments*/
MODULE_AUTHOR("TEAM VEDA");
MODULE_DESCRIPTION("FIRST MODULE");
MODULE_LICENSE("GPL");

EXPORT_NO_SYMBOLS;
static int val=0; //do not export to ksyms

MODULE_PARM(val, "i");
MODULE_PARM_DESC(val, "INTIALISE ME AT INSERTION TIME");
MODULE_SUPPORTED_DEVICE("NULL");

void func()
{
	printk("func invoked\n ");
	printk(" val = %d",val);
}

void func1()
{
	printk(" func 1 invoked\n");
}	

int myinit(void)
{
	printk(" module inserted\n ");
	func();
	return 0;
}

void myexit(void)
{
	printk(" module removed\n ");
}

module_init(myinit);
module_exit(myexit);
