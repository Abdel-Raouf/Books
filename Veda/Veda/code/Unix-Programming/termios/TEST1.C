/* about terminals.
Version : 1.0
Author : Team -C
*/
# include <termios.h>
# include <unistd.h>
# include <sys/ioctl.h>
# include <stdio.h>
# include <fcntl.h>

main(){
	int fd;
	fd = open("./test1.c",O_RDWR);
	printf(" fd = %d\n",fd);
	if( isatty(0) )
		printf(" File descriptor 0 is a tty\n");
	if( isatty(1) )
		printf(" File descriptor 1 is a tty\n");
	if( isatty(2) )
		printf(" File descriptor 2 is a tty\n");
	if( isatty(fd) )
		printf(" File descriptor 3 is a tty\n");
}
