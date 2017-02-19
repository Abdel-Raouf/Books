/*Module exporting choosen symbols
Author:Team-c
Version:1.0
*/


#define MODULE
#define __KERNEL__

#define EXPORT_SYMTAB //access to kernel symbol table

#include<linux/module.h>
#include<linux/versions.h>
#include<linux/kernel.h>
#include<linux/init.h>

/*Kernel module Comments*/
MODULE_AUTHOR("TEAM VEDA");
MODULE_DESCRIPTION("FIRST MODULE");
MODULE_LICENSE("GPL");

void func();

EXPORT_SYMBOL(func); //func exported

static int val=300; //do not export to ksyms

void func()
{
	printk("func invoked\n ");
	printk(" val = %d",val);
}

void func1()
{
	printk("<6> func 1 invoked\n");
}	

int myinit(void)
{
	printk("<5> module inserted\n ");
	return 0;
}

void myexit(void)
{
	printk("<4> module removed\n ");
}

module_init(myinit);
module_exit(myexit);
//Note: All the log levels are defined as part of /linux/kernel.h
