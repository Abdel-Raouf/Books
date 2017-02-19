 
#include <linux/config.h>
#include <linux/module.h>
#include <linux/moduleparam.h>
#include <linux/init.h>

#include <linux/sched.h>
#include <linux/kernel.h>
#include <linux/fs.h>     /* everything... */
#include <linux/proc_fs.h>
#include <linux/errno.h>  /* error codes */
#include <linux/workqueue.h>
#include <linux/preempt.h>
#include <linux/interrupt.h> /* tasklets */
#include <linux/jiffies.h> 

#define MODULE_NAME "BH"

MODULE_LICENSE("Dual BSD/GPL");

static struct proc_dir_entry *example_dir;
static int counter=0;



/* Work Queue Stuff Starts from here*/
//DECLARE_WORK(wq,wq_func,0);
static void wq_func(void *);
static struct work_struct jiq_work;
void wq_func(void *arg)
{
        printk("%ld %d %s\n", jiffies,counter++,current->comm);
        if( counter < 100 )
                schedule_work(&jiq_work );
	else
	  	counter=0;
}

static int jiq_read_wq(char *buf, char **start, off_t offset,
                   int len, int *eof, void *data)
{
	jiq_work.data=0;
	jiq_work.func=wq_func;
	schedule_work(&jiq_work);
	return 0;
}


/*We can declare tasklet like this also
	static void jiq_func(void);
	static DECLARE_TASKLET(jiq_tasklet, jiq_func, NULL);
*/

static void jiq_func(unsigned long);
static struct tasklet_struct jiq_tasklet;

static void jiq_func(unsigned long data)
{
 printk("%ld %d %s\n",jiffies,counter++,current->comm);
 if(counter<50)
	tasklet_schedule(&jiq_tasklet);	
  else
	counter=0;
}



static int jiq_read_tasklet(char *buf, char **start, off_t offset, int len,
                int *eof, void *data)
{
	jiq_tasklet.state=0;
	jiq_tasklet.func=jiq_func;
	tasklet_schedule(&jiq_tasklet);
	return 0;
}




/*
 * The Timer Stuff Starts here 
 */
static struct timer_list jiq_timer;

static void jiq_jiff(unsigned long data)
{
 printk("%ld %d %s\n", jiffies, counter++,current->comm);

        if( counter < 100 )
	add_timer(&jiq_timer);
	else
	counter=0;
	
	}

static int jiq_read_run_timer(char *buf, char **start, off_t offset,
                   int len, int *eof, void *data)
{
	jiq_timer.data=0;
	init_timer(&jiq_timer);
	printk("\n Timer Initialized\n");
	jiq_timer.function=jiq_jiff;

	add_timer(&jiq_timer);
	return 0;
}



/*
 * the init/clean material
 */

static int jiq_init(void)
{
	static int rv=0;
	example_dir = proc_mkdir(MODULE_NAME, NULL);
        if(example_dir == NULL) {
                rv=-ENOMEM;
		goto out;
        }

        example_dir->owner = THIS_MODULE;
	create_proc_read_entry("jiqtimer", 0644, example_dir, jiq_read_run_timer, NULL);
	create_proc_read_entry("jiqtasklet", 0644, example_dir, jiq_read_tasklet, NULL);
	create_proc_read_entry("jiqwq", 0644,example_dir, jiq_read_wq, NULL);

	/* this line is in jiq_init() */
	INIT_WORK(&jiq_work, wq_func, NULL);


	printk("\n Module Inserted\n");
	return 0; /* succeed */
out : return rv;
}


static void jiq_cleanup(void)
{
	del_timer(&jiq_timer);
	remove_proc_entry("jiqtimer", example_dir);
	remove_proc_entry("jiqtasklet", example_dir);
	remove_proc_entry("jiqwq", example_dir);
	remove_proc_entry(MODULE_NAME,NULL);
	printk("\n Module Removed\n");
}

module_init(jiq_init);
module_exit(jiq_cleanup);
