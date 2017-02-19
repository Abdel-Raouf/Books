/*First module program
Author:Team-c
Version:1.0
*/

#ifndef MODULE
	#define MODULE
#endif
#ifndef __KERNEL__
	#define __KERNEL__
#endif


#include<linux/module.h>
#include<linux/version.h>
#include<linux/kernel.h>
#include<linux/init.h>
#include<linux/slab.h>


#define SUCCESS 0

kmem_cache_t *cache_ptr;
int *ret;
int mem=200;
void func();

void func()
{
ret=kmem_cache_alloc(cache_ptr,GFP_KERNEL);

}

int init_mod(void)
{
cache_ptr=kmem_cache_create("cache_mem",mem,0,SLAB_HWCACHE_ALIGN,NULL,NULL);

if(cache_ptr==NULL)
	return -ENOMEM;

printk("\nModule loaded");
func();
return SUCCESS;
}

void cleanup_mod(void)
{
printk("\n Module removed");
kmem_cache_free(cache_ptr,ret);
kmem_cache_destroy(cache_ptr);
}

module_init(init_mod);
module_exit(cleanup_mod);

/*Kernel module Comments*/
MODULE_AUTHOR("TEAM VEDA");
MODULE_DESCRIPTION("FIRST MODULE");
MODULE_LICENSE("GPL");
