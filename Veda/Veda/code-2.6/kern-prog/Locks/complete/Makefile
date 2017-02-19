obj-m :=complete.o

KDIR = /lib/modules/$(shell uname -r)/build
all:
	$(MAKE) -C $(KDIR) SUBDIRS=$(PWD) modules

clean:
	rm  -rf *.ko *.mod.* *.o .*c .*t

