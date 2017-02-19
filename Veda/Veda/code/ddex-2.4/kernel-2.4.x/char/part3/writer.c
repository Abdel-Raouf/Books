/* App to write to device*/
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>

#include "veda_char_device.h"

int main()
{
	int fd, i;
	char my_message[22];
	unsigned long size;

	strcpy(my_message, "Veda Solutions");

	/* open the device for read/write/lseek */
	printf("[%d] - Opening device veda_chr_driver\n", getpid() );
	fd = open( "/dev/veda_cdrv", O_RDWR );
	if( fd < 0 ) {
		printf("Device could not be opened\n");
		return 1;
	}	
	printf("Device opened with ID [%d]\n", fd);
	
	ioctl(fd, VEDA_GET_SIZE, &size);
	printf("Size of the device = %d\n", size);

	printf("Writing [%s]\n", my_message );
	/* write the contents of my buffer into the device */
	size = (unsigned long)write( fd, my_message, strlen(my_message) );
	printf("Bytes written %d\n", size);
	bzero( my_message, 20 );
	read( fd, my_message, 20 );
	printf("Written [%s] \n", my_message );
	
	/* Close the device */
	close(fd);
	
	/* Thats all folks */
	exit(0);
}

