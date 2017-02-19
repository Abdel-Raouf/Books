/* implementation of Block Device Driver
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

#define DEVICE_NAME "blkdev_block"

#define BLOCK__SIZE 	2048
#define BLOCK__BLKSIZE 	1024
#define HARDSECT	 512
#define RAHEAD 		   2
#define MAX__SECTORS 	   1

#define DEVICE_COMMAND(i) (i==READ) ? "READ" : "WRITE"

#include <linux/blkdev.h>

/* Module parameters */
static unsigned long disk_size = 4096; /* bytes */
MODULE_PARM(disk_size, "i");
MODULE_PARM_DESC(disk_size, "Memory space in bytes");

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
	printk("\n\n\n");
	if( disk_size < 512 ) {
		printk("disk_size should be atleast 512 bytes\n");
		return -ENOSPC;
	}
	printk("%s wants %u bytes of memory. Checking for memory\n",
			DEVICE_NAME, disk_size );
	blkdev_buffer = (u8 *)vmalloc( sizeof(u8)*disk_size ); 
	if( !blkdev_buffer ) {
		printk("Memory space not available.\n");
		return -ENOSPC;
	}
	printk("%u bytes space found at %u\n", disk_size, blkdev_buffer);
	
	/* Register block device functions */
	blkdev_ops.open = blkdev_open;
	blkdev_ops.release = blkdev_release;
	blkdev_ops.ioctl = blkdev_ioctl;
	blkdev_ops.check_media_change = blkdev_check_media_change;
	blkdev_ops.revalidate = blkdev_revalidate;

	/* register this block device driver */
	blkdev_major = register_blkdev(0, DEVICE_NAME, &blkdev_ops);

	if( blkdev_major < 0 ) {
		printk("Block device registration failed\n\n");
		kfree( blkdev_buffer );
		return blkdev_major;
	}
	printk("%s registered with major number: %d\n", DEVICE_NAME, blkdev_major);
	
	/* Initialize arrays for this device */
	blk_size[blkdev_major] = create_int_array(1, BLOCK__SIZE);
	blksize_size[blkdev_major] = create_int_array(1, BLOCK__BLKSIZE);
	hardsect_size[blkdev_major] = create_int_array(1, HARDSECT);
	read_ahead[blkdev_major] = RAHEAD;
	max_sectors[blkdev_major] = create_int_array(1, MAX__SECTORS);
	
	/* Initalize request queue and set the request handler */
	blk_init_queue(BLK_DEFAULT_QUEUE(blkdev_major), blkdev_request);
	
	/* register each block device - for the sake of partitions */
	register_disk(0, MKDEV(blkdev_major, 0), 1, &blkdev_ops, BLOCK_SIZE<<1 );
	
	
	return SUCCESS;
}

/* exit point */
static __exit void cleanup_module(void)
{
	blk_cleanup_queue( BLK_DEFAULT_QUEUE(blkdev_major) );
	if( blk_size[blkdev_major] != 0 ) {
		kfree(blk_size[blkdev_major]);
		blk_size[blkdev_major] = 0;
	}
	if( blksize_size[blkdev_major] != 0 ) {
		kfree(blksize_size[blkdev_major]);
		blksize_size[blkdev_major] = 0;
	}
	if( hardsect_size[blkdev_major] != 0 ) {
		kfree(hardsect_size[blkdev_major]);
		hardsect_size[blkdev_major] = 0;
	}
	read_ahead[blkdev_major] = 0;
	fsync_dev( MKDEV(blkdev_major,0) );
	vfree( blkdev_buffer );
	unregister_blkdev( blkdev_major, DEVICE_NAME );
}


static int *create_int_array(int n, int value)
{
	int *ptr = 0, i = 0;
	ptr = (int *)kmalloc( sizeof(int) * n, GFP_KERNEL );
	if( !ptr ) ptr = NULL;
	for(i=0; i<n; i++) {
		*(ptr+i) = value;
	}
	return ptr;
}

static void blkdev_request(request_queue_t *q)
{
	int status;
	while(1) {
		INIT_REQUEST;
#ifdef DESC_REQUEST
		printk("\nRequest Details\n");
		printk("Request Command: %s(%i)\n", 
				DEVICE_COMMAND(CURRENT->cmd),
				CURRENT->cmd );
		printk("Request Sector : %li\n", CURRENT->sector);
		printk("Request nr_sectors: %li\n", 
				CURRENT->current_nr_sectors );
#endif
		spin_lock( &blkdev_spin_lock );
		status = blkdev_transfer(CURRENT);
		spin_unlock( &blkdev_spin_lock );

		end_request(status);
	}
}

static int blkdev_transfer(struct request *req)
{
	int size, start;
	u8 *ptr;

	ptr = blkdev_buffer + req->sector * HARDSECT;
	size = req->current_nr_sectors * HARDSECT;
	start = req->sector*HARDSECT;
	
	if( start+size > disk_size ) {
		printk("WARNING [%s]: Request past EOF in device\n",
				DEVICE_NAME);
		return 0;
	}

	switch( req->cmd ) {
	case READ:
		memcpy(req->buffer, ptr, size);
		return 1;
	case WRITE:
		memcpy(ptr, req->buffer, size);
		return 1;
	}
	return 0; /* should not come here */
}

static int blkdev_open(struct inode* inode, struct file* fptr)
{
	spin_lock( &blkdev_spin_lock );
	MOD_INC_USE_COUNT;
	spin_unlock( &blkdev_spin_lock );
	return 0;
}

static int blkdev_release(struct inode* inode, struct file* fptr)
{
	spin_lock( &blkdev_spin_lock );
	MOD_DEC_USE_COUNT;
	spin_unlock( &blkdev_spin_lock );
	return 0;
}

static int blkdev_ioctl(struct inode* inode, struct file* fptr, 
		unsigned cmd, unsigned long arg)
{
	int err;
	unsigned long size;
	struct hd_geometry geo;
	
	switch( cmd ) {
	case BLKGETSIZE:
		if( !arg ) return -EINVAL;
		err = !access_ok(VERIFY_WRITE, arg, sizeof(long));
		if( err ) return -EFAULT;
		size = disk_size/HARDSECT;
		if( copy_to_user((long *)arg, &size, sizeof(long) ) )
			return -EFAULT;
		return 0;
	case BLKRRPART:
		return -ENOTTY;
	case HDIO_GETGEO:
	        err = ! access_ok(VERIFY_WRITE, arg, sizeof(geo));
	        if (err) return -EFAULT;
		        size = BLOCK__SIZE * BLOCK__BLKSIZE / HARDSECT;
	        geo.cylinders = (size & ~0x3f) >> 6;
	        geo.heads = 4;
	        geo.sectors = 16;
	        geo.start = 4;
	        if (copy_to_user((void *) arg, &geo, sizeof(geo)))
	            return -EFAULT;
	        return 0;
	default:
		return blk_ioctl(inode->i_rdev, cmd, arg);
	}
	return 0; /* should never be executed */
}

static int blkdev_check_media_change(kdev_t dev)
{
	return 0;
}

static int blkdev_revalidate(kdev_t dev)
{
	return 0;
}

MODULE_AUTHOR("Team veda");
MODULE_DESCRIPTION("Block Driver");
MODULE_LICENSE("GPL");

