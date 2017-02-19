/*First module program
Author:Team-c
Version:1.0
*/

#ifndef MODULE
	#define MODULE
#endif
#ifndef __KERNEL__
	#define __KERNEL__
#endif


#include<linux/module.h> //
#include<linux/version.h>
#include<linux/kernel.h>
#include<linux/init.h>

void func(void);

int val=300;

void func()
{
	printk("func invoked\n ");
	printk(" val = %d",val);
}	

int init_mod(void)
{
	printk("module inserted\n ");
	return 0;
}

void cleanup_mod(void)
{
	printk("module removed\n ");
}

module_init(init_mod);
module_exit(cleanup_mod);

/*Kernel module Comments*/
MODULE_AUTHOR("TEAM VEDA");
MODULE_DESCRIPTION("FIRST MODULE");
MODULE_LICENSE("GPL");
