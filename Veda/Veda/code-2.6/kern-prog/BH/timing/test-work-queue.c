#ifndef __KERNEL__
 #define __KERNEL__
#endif

#ifndef MODULE
 #define MODULE
#endif


/* Demonstration of WORK-QUEUES*/
#include<linux/kernel.h>
#include<linux/init.h>
#include<linux/module.h>
#include<linux/workqueue.h>

int wq_init(void);
void wq_exit(void);
void wq_func(void *);


MODULE_LICENSE("GPL");
module_init(wq_init);
module_exit(wq_exit);

static int counter;

DECLARE_WORK(wq,wq_func,0);
//create_workqueue(myqueue);

void wq_func(void *arg)
{
	printk("%ld %d %s\n", jiffies,counter++,current->comm);
        if( counter < 1000 )
                schedule_work(&wq );
}

int  wq_init(void)
{
	printk("\nModule Inserted\n");
	//INIT_WORK(wq,wq_func,0);
	schedule_work(&wq);
		
//	schedule_delayed_work(&wq,jiffies+100);
	return 0;	
}

void wq_exit()
{
printk("\nModule Removed");
}

/* Browse linux/workqueue.h for further symbols.*/
