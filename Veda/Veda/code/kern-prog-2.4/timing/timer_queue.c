/*timer_queue.c
** Author: Veda
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
#include <linux/sched.h>
#include <linux/tqueue.h>

static int counter = 0;
struct tq_struct time_task;

/* this is the task that will be run whenever there is no other
   pending task in the queue */
static __exit void print_fn(void *data)
{
	printk("%20d %3d %10s\n", jiffies, counter++, current->comm);
	if( counter < 1000 ) 
		queue_task(&time_task, &tq_immediate);
}

/* entry point */
static __init int init_module(void)
{
	time_task.routine = print_fn;
	time_task.data = 0;
	printk("%20s %3s %10s\n", "jiffies", "C", "cmd");
	queue_task(&time_task, &tq_timer);
	return SUCCESS;
}

/* exit point */
static __exit void cleanup_module(void)
{

}


MODULE_AUTHOR("VEDA");
MODULE_DESCRIPTION("Example");
MODULE_LICENSE("GPL");
