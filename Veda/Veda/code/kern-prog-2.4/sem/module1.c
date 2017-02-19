/* Shows Usage of Semaphores in Modules
Author;Team-c
Version:1.0
*/

#ifndef __KERNEL__
	#define __KERNEL__
#endif

#ifndef MODULE
	#define MODULE
#endif

#define EXPORT_SYMTAB

#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/slab.h>
#include <asm/semaphore.h>

static __exitdata struct semaphore my_semaphore;

static __exit     int get_semaphore(void);
static __exit     void leave_semaphore(void);

EXPORT_SYMBOL_NOVERS(get_semaphore);
EXPORT_SYMBOL_NOVERS(leave_semaphore);

static __init int init_module(void)
{
	init_MUTEX( &my_semaphore );
//	atomic_set(&(my_semaphore.count),10);
       
	printk("\n\nModule1: I have the semaphore\n");
	down( &my_semaphore );
		printk("Getting  the semaphore and the count is:%d\n",my_semaphore.count);
	up( &my_semaphore );
	printk("Released semaphore and count now is:%d\n",my_semaphore.count);
	printk("Module1: Exported semaphore [my_semaphore]\n\n");
	return 0;
}

static __exit void cleanup_module(void)
{
	printk("\n\nSemaphore Destroyed\n\n");
}

static __exit int get_semaphore(void)
{
	return down_trylock( &my_semaphore );
}

static __exit void leave_semaphore(void)
{
	up( &my_semaphore );
}


