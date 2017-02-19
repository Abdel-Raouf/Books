#ifndef __KERNEL__
	#define __KERNEL__
#endif

#ifndef MODULE
	#define MODULE
#endif

#define EXPORT_SYMTAB

#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/init.h>
#include <asm/semaphore.h>


/**
 * We would like to use SIGNAL and WAIT macros instead of down and up
 * primitives provided by the Linux headers
 *
 * SIGNAL primitive says that the semaphore usage is finished
 * WAIT   primitive says that the current process should wait for 
 * 	  semaphore to be available
 * 	  
 * */

extern  int get_semaphore(void);
extern  void leave_semaphore(void);


#define SIGNAL() leave_semaphore()
#define WAIT()   get_semaphore()


static int myinit(void)
{
	printk("Module2: Waiting for semaphore\n");
	if( WAIT() ) {
		printk("Module2: Not got semaphore\n");
		return -1;

	}
	printk("Module2: Got semaphore now\n");
	return 0;
}

static void myexit(void)
{
	SIGNAL();
	printk("\n Module2 says bye");
}
module_init(myinit);
module_exit(myexit);
MODULE_LICENSE("GPL");
