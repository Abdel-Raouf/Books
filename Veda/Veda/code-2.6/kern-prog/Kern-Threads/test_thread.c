#include <linux/init.h>
#include <linux/module.h>
#include <linux/wait.h>

extern int myvar;
extern int myevnt;
extern wait_queue_head_t my_queue;

int init_module()
{
	myvar=40;
	myevnt=1;
	wake_up_interruptible(&my_queue);
	return 0;
}

void cleanup_module()
{
printk("\n Module Removed\n");
}
