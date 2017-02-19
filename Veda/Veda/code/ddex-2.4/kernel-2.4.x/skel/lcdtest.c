 
/* Test App for mylcd driver
Author : Team -C
Version : 1.0
*/

# include <stdio.h>
# include <sys/types.h>
# include <fcntl.h>
main(){
	int fd,n;
	char buf[12]="hello world";
	fd = open("/dev/mylcd",O_RDWR);
	printf("\n fd = %d",fd);
	write(fd,buf,12);
	n=read(fd,buf,10);
	printf("no of bytes read are %d \n",n);
	
}			
