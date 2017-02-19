/*  Module demonstrating a simple USB driver */

#ifndef __KERNEL__
	#define __KERNEL__
#endif 

#ifndef MODULE
	#define MODULE
#endif

#define USB_KBD_VENDOR_ID  0x1241
#define USB_KBD_PRODUCT_ID 0x1603

#include <linux/kernel.h>
#include <linux/slab.h>
#include <linux/module.h>
#include <linux/input.h>
#include <linux/init.h>
#include <linux/usb.h>

struct usb_kbd {
	struct input_dev dev;
	char name[128];
        char phys[64];
        unsigned char *new;
	struct urb *irq;
};

static int usb_kbd_init(void);
static void usb_kbd_exit(void);

static void usb_kbd_irq(struct urb *urb, struct pt_regs *regs)
{
	printk("\n Interrupt received from our USB KEYBOARD");
	
}

static int usb_kbd_probe(struct usb_interface *iface,
				const struct usb_device_id *id)
{
	int pipe , maxpkt; 
	struct usb_kbd *kbd;
	char *buf;
	char path[64];
	printk("\n In PROBE");

        // Determine the USB DEVICE info for the given INTERFACE INFO
	struct usb_device * dev = interface_to_usbdev(iface);

	/* For Holding  a specific interface of the usb device */
	struct usb_host_interface *interface;

	struct usb_endpoint_descriptor *endpoint;

        interface = iface->cur_altsetting;

	/* Checking for the existence of endpoints for data transfer*/
	if (interface->desc.bNumEndpoints != 1)
                return -ENODEV;

	/* Hold the endpoint descriptor of the device for data transfer*/
	  endpoint = &interface->endpoint[0].desc;

	/* Check whether the endpoint is an Interrupt Endpoint or not.
	   This is because all hid devices possess Interrupt Endpoints
	   for interacting with the host */

	if (!(endpoint->bEndpointAddress & 0x80))
                return -ENODEV;
        if ((endpoint->bmAttributes & 3) != 3)
                return -ENODEV;

	 // Pipe for data reception.
  	pipe = usb_rcvintpipe(dev, endpoint->bEndpointAddress);
	
        // Pipe can receive maxpkt size of data.
	maxpkt = usb_maxpacket(dev,pipe,usb_pipeout(pipe));
	
	// Allocate memory for our structures.

	if (!(kbd = kmalloc(sizeof(struct usb_kbd), GFP_KERNEL)))
                return -ENOMEM;
        memset(kbd, 0, sizeof(struct usb_kbd));
	
	if (!(kbd->irq = usb_alloc_urb(0, GFP_KERNEL)))
                return -1;

	usb_fill_int_urb(kbd->irq, dev, pipe,
                         kbd->new, (maxpkt > 8 ? 8 : maxpkt),
                         usb_kbd_irq, kbd, endpoint->bInterval);

        usb_make_path(dev, path, 64);
        sprintf(kbd->phys, "%s/input0", path);



	  if (!(buf = kmalloc(63, GFP_KERNEL))) {
                usb_free_urb(kbd->irq);
                kfree(kbd);
                return -ENOMEM;
        }

        if (dev->descriptor.iManufacturer &&
                usb_string(dev, dev->descriptor.iManufacturer, buf, 63) > 0)
                        strcat(kbd->name, buf);
        if (dev->descriptor.iProduct &&
                usb_string(dev, dev->descriptor.iProduct, buf, 63) > 0)
                        sprintf(kbd->name, "%s %s", kbd->name, buf);

      /*  if (!strlen(kbd->name))
                sprintf(kbd->name, "USB Keyboard %04x:%04x",
                        kbd->dev.id.vendor, kbd->dev.id.product);*/

        kfree(buf);
	 printk(KERN_INFO "input: %s on %s\n", kbd->name, path);

        usb_set_intfdata(iface, kbd);
        return 0;
}

static void usb_kbd_disconnect(struct usb_interface *intf)
{
        struct usb_kbd *kbd = usb_get_intfdata (intf);

        usb_set_intfdata(intf, NULL);
        if (kbd) {
                usb_kill_urb(kbd->irq);
		usb_free_urb(kbd->irq);
                kfree(kbd);
        }
}

static struct usb_device_id usb_kbd_id_table [] = {
        { USB_DEVICE(USB_KBD_VENDOR_ID, USB_KBD_PRODUCT_ID) },
        { }                                     /* Terminating entry */
};

MODULE_DEVICE_TABLE (usb, usb_kbd_id_table);

static struct usb_driver usb_kbd_driver = {
        .owner =        THIS_MODULE,
        .name =         "usbkbd-demo",
        .probe =        usb_kbd_probe,
        .disconnect =   usb_kbd_disconnect,
        .id_table =     usb_kbd_id_table,
};



static int usb_kbd_init(void)
{
	int ret;
	ret = usb_register(&usb_kbd_driver);
	return ret;
}

static void usb_kbd_exit()
{
	usb_deregister(&usb_kbd_driver);
} 
module_init(usb_kbd_init);
module_exit(usb_kbd_exit);
