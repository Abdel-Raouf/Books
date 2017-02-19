/*Block Device Driver Template
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
#include <linux/config.h>
#include <linux/sched.h>
#include <linux/slab.h>
#include <linux/errno.h>
#include <linux/timer.h>
#include <linux/types.h>
#include <linux/fcntl.h>
#include <linux/module.h>
#include <linux/init.h>
#include <linux/fs.h>
#include <linux/hdreg.h>
#include <asm/system.h>
#include <asm/uaccess.h>
#include <linux/blkpg.h>
#include <linux/vmalloc.h>

#define DEVICE_NAME "blkdv"

#define BLOCK__SIZE 	2048
#define BLOCK__BLKSIZE 	1024
#define HARDSECT	 512
#define RAHEAD 		   2
#define MAX__SECTORS 	   1

#define DEVICE_COMMAND(i) (i==READ) ? "READ" : "WRITE"

#include <linux/blkdev.h>

/* Module parameters */
static unsigned long disk_size = 65536; /* bytes */

/* Module variables */
static struct block_device_operations blkdev_ops;
static int blkdev_major;
static u8 *blkdev_buffer = 0;
static spinlock_t blkdev_spin_lock;

/* Request handler */
static void blkdev_request(request_queue_t *q);

#define MAJOR_NR 	blkdev_major
#define DEVICE_INTR 	blkdev_interrupt
#define DEVICE_NO_RANDOM
#define DEVICE_REQUEST 	blkdev_request
#define DEVICE_OFF(d)
#include <linux/blk.h>

/* device file operations structure */
static int blkdev_open(struct inode*, struct file*);
static int blkdev_release(struct inode*, struct file*);
static int blkdev_ioctl(struct inode*, struct file*, unsigned, unsigned long);
static int blkdev_check_media_change(kdev_t);
static int blkdev_revalidate(kdev_t);

/* utility functions */
static int *create_int_array(int n, int value);
static int blkdev_transfer(struct request *req);

/* entry point */
static __init int init_module(void)
{
	/*
	Fill your code here
	*/	
}

/* exit point */
static __exit void cleanup_module(void)
{
	/*
	Fill your code here
	*/	
}

static void blkdev_request(request_queue_t *q)
{
	/*
	Fill your code here
	*/	
}

static int blkdev_open(struct inode* inode, struct file* fptr)
{
	/*
	Fill your code here
	*/	
}

static int blkdev_release(struct inode* inode, struct file* fptr)
{
	/*
	Fill your code here
	*/	
}

static int blkdev_ioctl(struct inode* inode, struct file* fptr, 
		unsigned cmd, unsigned long arg)
{
	/*
	Fill your code here
	*/	
}

static int blkdev_check_media_change(kdev_t dev)
{
	/*
	Fill your code here
	*/	
}

static int blkdev_revalidate(kdev_t dev)
{
	/*
	Fill your code here
	*/	
}

MODULE_AUTHOR("Team Veda");
MODULE_DESCRIPTION("Example Block Driver");
MODULE_LICENSE("GPL");

