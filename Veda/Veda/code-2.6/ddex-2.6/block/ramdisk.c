/*
 * A sample,simple block driver.
 *
 * 
 */

#include <linux/module.h>
#include <linux/moduleparam.h>
#include <linux/init.h>

#include <linux/kernel.h> /* printk() */
#include <linux/fs.h>     /* everything... */
#include <linux/errno.h>  /* error codes */
#include <linux/types.h>  /* size_t */
#include <linux/vmalloc.h>
#include <linux/genhd.h>
#include <linux/blkdev.h>
#include <linux/hdreg.h>
#include <linux/device.h>
#include <asm/uaccess.h>
#include <linux/blkpg.h>
#include <linux/version.h>

MODULE_LICENSE("Dual BSD/GPL");
//static char *Version = "1.3";
static int major_num = 0;
module_param(major_num, int, 0);
static int hardsect_size = 512;
module_param(hardsect_size, int, 0);
static int nsectors = 1024;  /* How big the drive is */
module_param(nsectors, int, 0);

#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,14)
static struct class_simple *veda_class=NULL;//udev support
#else
static struct class *veda_class=NULL;//udev support
#endif

static unsigned long disk_size = 4096;
/*
 * The Disk private data structure
 */
static struct sbd_device {
    unsigned long size;//size of the device
    spinlock_t lock;
    u8 *data;//array to store disk data
    struct gendisk *gd;//kernel representation of device
} Device;
 
dev_t mydev;

/*
 * We can tweak our hardware sector size, but the kernel talks to us
 * in terms of small sectors, always.
 */
#define KERNEL_SECTOR_SIZE 512

int sbd_ioctl (struct inode *inode, struct file *filp,
                 unsigned int cmd, unsigned long arg);

static void sbd_request(request_queue_t *q);


static void sbd_transfer(struct sbd_device  *dev, unsigned long sector,
		unsigned long nsect, char *buffer, int write);
/*
 * Our request queue.
 */
static struct request_queue *Queue;



/*
 * The device operations structure.
 */
static struct block_device_operations sbd_ops = {
    .owner=THIS_MODULE,
    .ioctl =sbd_ioctl
};

static int __init sbd_init(void)
{
	printk("\n Module Inserted\n");	
/*
 * Set up our device information.
 */
    Device.size = nsectors*hardsect_size;
    spin_lock_init(&Device.lock);
    Device.data = vmalloc(Device.size);
    if (Device.data == NULL)
	return -ENOMEM;
/*
 * Get a request queue.
 */
    Queue = blk_init_queue(sbd_request, &Device.lock);
    if (Queue == NULL)
	    goto out;
    blk_queue_hardsect_size(Queue, hardsect_size);
/*
 * Get registered.
 */
    major_num = register_blkdev(major_num, "sbd");
    if (major_num <= 0) {
	printk(KERN_WARNING "sbd: unable to get major number\n");
	goto out;
    }

	mydev=MKDEV(major_num,0);

/* Let's Start Udev stuff */

#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,14)
        veda_class = class_simple_create(THIS_MODULE,"Veda");
        if(IS_ERR(veda_class)){
                printk(KERN_ERR "Error registering veda class\n");
        }

        class_simple_device_add(veda_class,mydev,NULL,"sbd");
#endif

#if LINUX_VERSION_CODE >= KERNEL_VERSION(2,6,14)
        veda_class = class_create(THIS_MODULE,"Veda");
        if(IS_ERR(veda_class)){
                printk(KERN_ERR "Error registering veda class\n");
        }

        class_device_create(veda_class,mydev,NULL,"sbd");
#endif

/*
 * And the gendisk structure.
 */
    Device.gd = alloc_disk(1);
    if (! Device.gd)
	goto out_unregister;
    Device.gd->major =major_num;
    Device.gd->first_minor =0;
    Device.gd->fops =&sbd_ops;
    Device.gd->private_data =&Device;
    strcpy (Device.gd->disk_name, "sbd0");
/* Convert sectors to 512  */
    set_capacity(Device.gd, nsectors*(hardsect_size/KERNEL_SECTOR_SIZE));
    Device.gd->queue = Queue;
    add_disk(Device.gd);

    return 0;

  out_unregister:
    unregister_blkdev(major_num, "sbd");
  out:
    vfree(Device.data);
    return -ENOMEM;
}

static void __exit sbd_exit(void)
{
    del_gendisk(Device.gd);
    put_disk(Device.gd);
#if LINUX_VERSION_CODE < KERNEL_VERSION(2,6,14)
	class_simple_device_remove(mydev);
        class_simple_destroy(veda_class);
#else
        class_device_destroy(veda_class,mydev);
        class_destroy(veda_class);		
#endif    
	unregister_blkdev(major_num, "sbd");
    	blk_cleanup_queue(Queue);
    	vfree(Device.data);
	printk("\n Module Removed\n");
}

static void sbd_request(request_queue_t *q)
{
    struct request *req;

    while ((req = elv_next_request(q)) != NULL) {
	if (! blk_fs_request(req)) {
	    printk (KERN_NOTICE "Skip non-CMD request\n");
	    end_request(req, 0);
	    continue;
	}
	sbd_transfer(&Device, req->sector, req->current_nr_sectors,
			req->buffer, rq_data_dir(req));
	end_request(req, 1);
    }
}



/*
 * Handle an I/O request.
 */
static void sbd_transfer(struct sbd_device *dev, unsigned long sector,
		unsigned long nsect, char *buffer, int write)
{
    unsigned long offset = sector*hardsect_size;
    unsigned long nbytes = nsect*hardsect_size;
    
    if ((offset + nbytes) > dev->size) {
	printk (KERN_NOTICE "sbd: Beyond-end write (%ld %ld)\n", offset, nbytes);
	return;
    }
    if (write)
	memcpy(dev->data + offset, buffer, nbytes);
    else
	memcpy(buffer, dev->data + offset, nbytes);
}

/*
 * Ioctl.
 */
int sbd_ioctl (struct inode *inode, struct file *filp,
                 unsigned int cmd, unsigned long arg)
{
	int err;
	unsigned long size;
	struct hd_geometry geo;

	switch(cmd) {
	/*
	 * The only command we need to interpret is HDIO_GETGEO, since
	 * we can't partition the drive otherwise.  We have no real
	 * geometry, of course, so make something up.
	 */
	    case HDIO_GETGEO:
		size = Device.size*(hardsect_size/KERNEL_SECTOR_SIZE);
		geo.cylinders = (size & ~0x3f) >> 6;
		geo.heads = 4;
		geo.sectors = 16;
		geo.start = 4;
		if (copy_to_user((void *) arg, &geo, sizeof(geo)))
			return -EFAULT;
		return 0;

	case BLKGETSIZE:
                if( !arg ) return -EINVAL;
                err = !access_ok(VERIFY_WRITE, arg, sizeof(long));
                if( err ) return -EFAULT;
                size = disk_size/hardsect_size;
                if( copy_to_user((long *)arg, &size, sizeof(long) ) )
                        return -EFAULT;
                return 0;

	
    }

    return -ENOTTY; /* unknown command */
}
	
module_init(sbd_init);
module_exit(sbd_exit);
