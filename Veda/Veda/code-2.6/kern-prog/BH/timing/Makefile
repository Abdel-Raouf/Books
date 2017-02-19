
#obj-m :=while_delay.o
obj-m  :=tsc.o
#obj-m :=test-work-queue.o

KDIR = /lib/modules/$(shell uname -r)/build
#KDIR = /usr/src/linux-2.6.12.2
all:
	$(MAKE) -C $(KDIR) SUBDIRS=$(PWD) modules

clean:
	rm  -rf *.ko *.mod.* *.o .*c .*t

