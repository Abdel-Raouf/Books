/* custom_queue.c
** Author : Veda
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
#include <linux/tqueue.h>
#include <linux/sched.h>

static unsigned long init_jiffies;
static unsigned long jif_delay = 3; /* 3 seconds */
static unsigned char stop = 0;
static int counter = 0;
static unsigned long time_counter = 0;

static struct tq_struct custom_tsk1, custom_tsk2;
static struct tq_struct timer_tsk;

DECLARE_TASK_QUEUE(custom_queue);


/* custom tasks */
static __exit void custom_task_1(void *data)
{
	printk("In custom task_1 at jiffies %d\n", jiffies);
	if( stop == 0 )
		queue_task( &custom_tsk1, &custom_queue );
}

static __exit void custom_task_2(void *data)
{
	printk("In custom task_2 at jiffies %d\n", jiffies);
	if( stop == 0 )
		queue_task( &custom_tsk2, &custom_queue );
}

/* task that goes into the timer queue */
static __exit void timer_task(void *data)
{
	time_counter++;
	if( time_counter > 3*HZ ) {
		time_counter = 0;
		printk("\n\nCounter: %d\n", counter);
		run_task_queue( &custom_queue );
		counter++;
	}
	if( counter > 10 ) 
		stop = 1;
	else
		queue_task( &timer_tsk, &tq_timer );
	
}

/* entry point */
static __init int init_module(void)
{
	init_jiffies = jiffies;
	
	/* prepare our custom task tq_structs */
	custom_tsk1.data = custom_tsk2.data = 0;
	custom_tsk1.routine = custom_task_1;
	custom_tsk2.routine = custom_task_2;
	/* Queue our tasks in our queue */
	queue_task( &custom_tsk1, &custom_queue);
	queue_task( &custom_tsk2, &custom_queue);
	
	/* queue the trigger task in the timer queue */
	timer_tsk.data = 0;
	timer_tsk.routine = timer_task;
	queue_task( &timer_tsk, &tq_timer );
	
	return SUCCESS;
}

/* exit point */
static __exit void cleanup_module(void)
{

}


MODULE_AUTHOR("VEDA");
MODULE_DESCRIPTION("Example");
MODULE_LICENSE("GPL");
