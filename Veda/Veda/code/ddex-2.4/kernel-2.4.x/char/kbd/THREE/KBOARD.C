/* Implementation of device Driver for keyboard, check Reamme.kb for 
details.
Version : 1.0
Author : Team - C.
*/

# define __KERNEL__
# define MODULE
#include <linux/module.h>
#include <linux/version.h>
#include <linux/kernel.h>
#include <linux/fs.h>    
#include <linux/malloc.h>
#include <linux/ioport.h>
#include <asm/io.h>
# include <asm/uaccess.h> //copy_from/to_user
# include "kboard.h"

# define KB_MAJOR 195
# define KB_INT  1

static unsigned char inuse;

static unsigned char some_data = 100;

DECLARE_WAIT_QUEUE_HEAD(kb_queue);

static unsigned char up_code;

int kb_open(struct inode *inode, struct file *filp)
{
	if(inuse)
		return -EBUSY;
	inuse = 1;// indicating this is in use.
	return 0;
}

int kb_release(struct inode *inode, struct file *filp)
{
	inuse = 0;
	return 0;
}

ssize_t kb_read(struct file *filp,char *buf, size_t count,
		    loff_t *f_pos)
{
	sleep_on(&kb_queue);	
	//interrupt handler will be waking up us.
	copy_to_user(buf,&up_code,1);
	return 1;
}
struct file_operations fops = {
 	read: kb_read,
	open: kb_open,
	release: kb_release,
};

static void kb_interrupt(int irq, void *dev_id, struct pt_regs *regs)
{
	unsigned char scancode;
	unsigned char status;
	scancode = read_kbd_input();
//	printk("scancode = %d\n",scancode);
	up_code = scancode;
	wake_up(&kb_queue);
}

int init_module(void)
{
	unsigned char data = 0;
	int result = 0;
	inuse = 0;
	result = register_chrdev(KB_MAJOR,"mykb",&fops);
if(request_irq(KB_INT,kb_interrupt,SA_SHIRQ, "mykb",(void *)&some_data)){
		printk(KERN_ERR "mykb: cannot register IRQ %d\n", 8);
		return -EIO;
	}
	printk("mykb interrupt registered\n");
	return 0;
}

void cleanup_module(void)
{
	free_irq(KB_INT,&some_data);
	unregister_chrdev(KB_MAJOR, "mykb");
}




