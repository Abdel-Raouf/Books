/*Dep module
Author:Team-c
Version:1.0
*/

#define MODULE
#define __KERNEL__

#include<linux/module.h>
#include<linux/version.h>
#include<linux/kernel.h>


int init_module(void)
{
	printk(" Calling Kernel symbol \n ");
	func();
	return 0;
}

void cleanup_module(void)
{
	printk(" cleanup invoked \n");
}


