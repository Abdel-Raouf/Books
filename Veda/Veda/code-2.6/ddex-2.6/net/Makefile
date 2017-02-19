obj-m :=lin_net_device.o
obj-m +=lin_net1.o
obj-m +=lin_net2.o

KDIR= /lib/modules/$(shell uname -r)/build

all:
	$(MAKE) -C $(KDIR) SUBDIRS=$(PWD) modules
clean:
	rm -rf *.o  *.mod.* .c* .t* 
clear:
	rm -rf *.o  *.mod.* .c* .t*  *.ko
	

