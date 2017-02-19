> gcc -fPIC jpgHandler.c

/*
	fPIC: option:

	Position Independent Code means that the generated machine code is not dependent on being located at a specific address in order to work.
	E.g. jumps would be generated as relative rather than absolute.

	Pseudo-assembly:

	PIC: This would work whether the code was at address 100 or 1000

	100: COMPARE REG1, REG2
	101: JUMP_IF_EQUAL CURRENT+10
	...
	111: NOP
	Non-PIC: This will only work if the code is at address 100

	100: COMPARE REG1, REG2
	101: JUMP_IF_EQUAL 111
	...
	111: NOP
*/

> gcc -nostdlib -shared -o libjphhandler.so.1 jpghandler.o -lc

/*
	-nostdlib
		This flag skips linking with runtime code(_init, _fini) and standard library(libc)
	-lc
		This flag links with C library exclusively
*/

> gcc -rdynamic -Dsize =10 UI.c viml.c config.c -o app

/*
	-rdynamic
		This flag builts executable PLT table in such a way that the
		entries of the table are visible to dynamic shared libraries
		used by the application.
		

		PLT stands for Procedure Linkage Table which is, put simply, 
		used to call external procedures/functions whose address isn't 
		known in the time of linking, and is left to be resolved by 
		the dynamic linker at run time.

		GOT stands for Global Offsets Table and is similarly used to 
		resolve addresses.
*/

/*
	RTLD_NOW is passed through dlopen() to invokes loader(/lib/ld-linux.so)
	to load and bind the library to process memory

	RTLD_LAZY is passed through dlopen() to invokes loader(/lib/ld-linux.so)
	to load and leave the library in memory. Bind it when the application 
	make calls to library symbols
*/