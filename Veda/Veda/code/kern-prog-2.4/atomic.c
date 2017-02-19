/*shows Atomic operations in modules
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
#include <asm/atomic.h>

/** initializes an atomic variable and performs some atomic operations on it.*/
static __init int init_module(void)
{
	unsigned long low1, low2;
	atomic_t av = ATOMIC_INIT(1); /* atomic variable - av */
	printk("Read av %d\n", atomic_read(&av));
	
	atomic_add(2, &av);
	printk("Read av %d\n", atomic_read(&av));
	
	atomic_sub(1, &av);
	printk("Read av %d\n", atomic_read(&av));
	
	atomic_inc(&av);
	printk("Read av %d\n", atomic_read(&av));
	
	
	return SUCCESS;
}

static __exit void cleanup_module(void)
{

}

MODULE_AUTHOR("Team Veda");
MODULE_DESCRIPTION("Atomic Example");

