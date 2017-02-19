/* Explains about how to write a char device driver
** Team: Veda
** Version: 1.0
*/

#ifndef __KERNEL__
	#define __KERNEL__
#endif

#ifndef MODULE
	#define MODULE
#endif

#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <asm/uaccess.h>
#include <linux/init.h>
#include <linux/sched.h>

MODULE_AUTHOR("VEDA");
MODULE_DESCRIPTION("Character Device Driver - Test");

/**
 * Instead of having init_module and cleanup_module
 * we will use the macros module_init and module_exit
 * to set the init and cleanup functions
 * */
static int init_char_device(void);
static void exit_char_device();

module_init(init_char_device);
module_exit(exit_char_device);

/**
 * Since this is a character device driver we need to provide
 * support for having read and write operations on this device.
 *
 * Once we do that the user will be able to use the standard
 * functions like fopen(), fclose() or open() and close() functons
 * provided in stdio.h or stdlib.h to read and write the device.
 *
 * So we will create a structure called char_device_file_ops
 * that maps functiality on to the function names in this driver
 * */

/* Forward declaration of functions */
static int char_device_open(struct inode *inode,
			    struct file  *file);
static int char_device_release(struct inode *inode,
		            struct file *file);
static int char_device_read(struct file *file, 
		            char *buf,
			    size_t lbuf,
			    loff_t *ppos);
static int char_device_write(struct file *file,
		            const char *buf,
			    size_t lbuf,
			    loff_t *ppos);
static loff_t char_device_lseek(struct file *file,
		            loff_t offset,
			    int orig);
/* Define the mapping structure */
static struct file_operations char_device_file_ops;

/**
 * The initialization routine.
 *
 * This function should register with Linux Kernel that this module
 * is a character device driver. 
 * 
 * To do that it first fills the fops structure with valid call 
 * back functions
 * 
 * After we register with the Linux Kernel the kernel gives back
 * a driver id which is stored in char_device_id
 * 
 * The name of the device  would be CHAR_DEVICE_NAME = "veda_cdrv"
 *
 * */
static int char_device_id;
#define CHAR_DEVICE_NAME "veda_cdrv"
#define MAX_LENGTH 4000
static char char_device_buf[MAX_LENGTH];

static int init_char_device(void)
{
	int i, index;

	char_device_file_ops.read = char_device_read;
	char_device_file_ops.write = char_device_write;
	char_device_file_ops.open = char_device_open;
	char_device_file_ops.release = char_device_release;
	char_device_file_ops.llseek = char_device_lseek;
	
	char_device_id = register_chrdev(0, CHAR_DEVICE_NAME,
				&char_device_file_ops );
	if( char_device_id < 0 ) {
		printk("Error registering device driver\n");
		return char_device_id;
	}
	printk("Device Registered: Major Number: [%d]\n", 
			char_device_id );
	for(i=0; i<MAX_LENGTH; i++) char_device_buf[i] = 0;
	char_device_buf[MAX_LENGTH] = '\0';
	return 0;
}

/**
 * This function should neatly unregister itself and return
 * */
static void exit_char_device(void)
{
	int id;
	id = unregister_chrdev(char_device_id,
			CHAR_DEVICE_NAME );
	if( id < 0 ) {
		printk("Error while unregistering\n");
	} else {
		printk("Module successfully unregistered\n");
	}
}

#define SUCCESS 0
/**
 * This function is called when a user wants to use this device
 * and has called the open function. 
 *
 * The function will keep a count of how many people 
 * tried to open it and increments it each time
 * this function is called
 *
 * The function prints out two pieces of information
 * 1. Number of times open() was called on this device
 * 2. Number of processes accessing this device right now
 *
 * Return value
 * 	Always returns SUCCESS
 * */
static int char_device_open(struct inode *inode,
			    struct file  *file)
{
	static int counter = 0;
	counter++;
	MOD_INC_USE_COUNT;
	printk("Number of times open() was called: %d\n", counter);
	printk("Number of processes using this device: %d\n", MOD_IN_USE );
	printk("Process id of the current process: %d\n", current->pid );
	return SUCCESS;
}

/**
 * This function is called when the user program uses close() function
 * 
 * The function decrements the number of processes currently
 * using this device. This should be done because if there are no
 * users of a driver for a long time, the kernel will unload 
 * the driver from the memory.
 *
 * Return value
 * 	Always returns SUCCESS
 * */
static int char_device_release(struct inode *inode,
		            struct file *file)
{
	MOD_DEC_USE_COUNT;
	return SUCCESS;
}

/**
 * This function is called when the user calls read on this device
 * It reads from a 'file' some data into 'buf' which
 * is 'lbuf' long starting from 'ppos' (present position)
 * Understanding the parameters
 * 	buf  = buffer
 * 	ppos = present position
 * 	lbuf = length of the buffer
 * 	file = file to read
 * The function returns the number of bytes(characters) read.
 * */
static int char_device_read(struct file *file, 
		            char *buf,
			    size_t lbuf,
			    loff_t *ppos)
{
	int maxbytes; /* number of bytes from ppos to MAX_LENGTH */
	int bytes_to_do; /* number of bytes to read */
	int nbytes; /* number of bytes actually read */

	maxbytes = MAX_LENGTH - *ppos;
	
	if( maxbytes > lbuf ) bytes_to_do = lbuf;
	else bytes_to_do = maxbytes;
	
	if( bytes_to_do == 0 ) {
		printk("Reached end of device\n");
		return -ENOSPC; /* Causes read() to return EOF */
	}
	
	nbytes = bytes_to_do - 
		 copy_to_user( buf, /* to */
			       char_device_buf + *ppos, /* from */
			       bytes_to_do ); /* how many bytes */
	*ppos += nbytes;
	return nbytes;	
}

/**
 * This function is called when the user calls write on this device
 * It writes into 'file' the contents of 'buf' starting from 'ppos'
 * up to 'lbuf' bytes.
 * Understanding the parameters
 * 	buf  = buffer
 * 	file = file to write into
 * 	lbuf = length of the buffer
 * 	ppos = present position pointer
 * The function returs the number of characters(bytes) written
 * */
static int char_device_write(struct file *file,
		            const char *buf,
			    size_t lbuf,
			    loff_t *ppos)
{
	int nbytes; /* Number of bytes written */
	int bytes_to_do; /* Number of bytes to write */
	int maxbytes; /* Maximum number of bytes that can be written */

	maxbytes = MAX_LENGTH - *ppos;

	if( maxbytes > lbuf ) bytes_to_do = lbuf;
	else bytes_to_do = maxbytes;

	if( bytes_to_do == 0 ) {
		printk("Reached end of device\n");
		return -ENOSPC; /* Returns EOF at write() */
	}

	nbytes = bytes_to_do -
	         copy_from_user( char_device_buf + *ppos, /* to */
				 buf, /* from */
				 bytes_to_do ); /* how many bytes */
	*ppos += nbytes;
	return nbytes;
}

/**
 * This function is called when lseek() is called on the device
 * The function should place the ppos pointer of 'file'
 * at an offset of 'offset' from 'orig'
 *
 * if orig = SEEK_SET
 * 	ppos = offset
 *
 * if orig = SEEK_END
 * 	ppos = MAX_LENGTH - offset
 *
 * if orig = SEEK_CUR
 * 	ppos += offset
 *
 * returns the new position
 * */
static loff_t char_device_lseek(struct file *file,
		            loff_t offset,
			    int orig)
{
	loff_t new_pos;
	switch( orig )
	{
	case 0: /* SEEK_SET: */
		new_pos = offset;
		break;
	case 1: /* SEEK_CUR: */
		new_pos = file->f_pos + offset;
		break;
	case 2: /* SEEK_END: */
		new_pos = MAX_LENGTH - offset;
		break;
	}	
	if( new_pos > MAX_LENGTH ) new_pos = MAX_LENGTH;
	if( new_pos < 0 ) new_pos = 0;
	file->f_pos = new_pos;
	return new_pos;
}

/* End of code */