obj-m :=rtl8139_min.o
obj-m +=rtl8139.o

KDIR= /lib/modules/$(shell uname -r)/build

all:
	$(MAKE) -C$(KDIR) SUBDIRS=$(PWD) modules

clean:
	rm -rf *.o *.ko *.mod.* .c* .t*

