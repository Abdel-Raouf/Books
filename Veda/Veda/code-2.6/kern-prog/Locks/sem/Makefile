obj-m  :=module1.o
obj-m  +=module2.o
obj-m  +=module3.o

KDIR = /lib/modules/$(shell uname -r)/build

all:
	$(MAKE) -C $(KDIR) SUBDIRS=$(PWD) modules

clean:
	rm  -rf *.ko *.mod.* *.o .*c .*t

