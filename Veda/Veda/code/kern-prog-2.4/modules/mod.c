/*First module program
Author:Team-c
Version:1.0
*/


#define MODULE
#define __KERNEL__

/*Kernel Comments
MODULE_AUTHOR("TEAM VEDA");
MODULE_DESCRIPTION("FIRST MODULE");
MODULE_LICENSE("GPL");*/

#include<linux/module.h>
#include<linux/version.h>
#include<linux/kernel.h>
#include<linux/init.h>

/*Kernel module Comments*/
MODULE_AUTHOR("TEAM VEDA");
MODULE_DESCRIPTION("FIRST MODULE");
MODULE_LICENSE("GPL");

int val=300;

void func()
{
	printk("func invoked\n ");
	printk(" val = %d",val);
}	

int init_module(void)
{
	printk("module inserted\n ");
	return 0;
}

void cleanup_module(void)
{
	printk("module removed\n ");
}

