obj-m  :=module.o
obj-m  +=writer1.o
obj-m  +=writer2.o
obj-m  +=reader1.o
obj-m  +=reader2.o

KDIR = /lib/modules/$(shell uname -r)/build

all:
	$(MAKE) -C $(KDIR) SUBDIRS=$(PWD) modules

clean:
	rm  -rf *.ko *.mod.* *.o .*c .*t

