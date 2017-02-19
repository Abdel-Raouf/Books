/*shows usage of linked lists in Modules
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

#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/init.h>
#include <linux/list.h>
#include <linux/slab.h>

/**
This is the list head. It is a pointer to the linked list rather
the first element in the linked list.
*/
struct list_head MY_list;

/**
MY_list is declared as the list head
*/
LIST_HEAD( MY_list );


/**
This is how each node in the linked list looks like.
*/
struct MY_struct {
	struct list_head MY_s_list;
	int value;
};

/**
This function is called when the module is loaded into the memory.
The function adds 20 numbers into the linked list. 
*/
int  myinit(void)
{
	struct MY_struct *ls;
	int i;
	/* Adding elements on to the list */
	for(i=0; i<20; i++) {
		ls = (struct MY_struct*)kmalloc(
				sizeof( struct MY_struct ),
				GFP_KERNEL );
		ls->value = i;
		list_add( 
			&ls->MY_s_list, /* What to add */
			&MY_list 	 /* Where to add */
		);
	}
	printk("\n\n\nInserted 20 elements into the list.\n");
	printk("The list will be displayed while unloading this module\n\n\n");
	return 0;
}

/**
The function is called when the module is unloaded from the memory.
The function lists out the 20 numbers that was previously inserted.
*/
void  myexit(void)
{
	/* Walk through the linked list */
	struct MY_struct *ls;
	struct list_head *list;

	list = &MY_list;
	/* check if list is empty */
	if( list_empty(list) )	{
		printk("List is empty\n");
		return;
	}

	while( (list = list->next) != &MY_list ) {
		ls = list_entry( list, /* Pointer to the list_head data type */
				 struct MY_struct, /* Type of structure */
				 MY_s_list /* name of the llist member */
			);
		printk("Found number: %d\n", ls->value );
		/* Delete the node from the linked list */
//		list_del( &ls->MY_s_list );
		/* Delete the structure that holds the list_head */
		kfree( ls );
	}

	/* Display bye message */
	printk("Good bye\n");
}
module_init(myinit);
module_exit(myexit);

MODULE_AUTHOR("Team Veda");
MODULE_LICENSE("GPL");

