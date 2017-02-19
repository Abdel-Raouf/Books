/* shows how terminals processes special characters.
Version : 1.0
Author : Team -C
*/
# include <termios.h>
# include <unistd.h>
# include <sys/ioctl.h>
# include <stdio.h>
# include <fcntl.h>

main(){
	char buf[100];
	int i;
	i = read(0,buf,100);
	printf(" read %d char's from terminal \n",i);
}
/* run this program by changing terminal setting using stty
	stty -cooked
	stty intr ^a  ( default is ^c ).
	stty kill ^b  ( default is ^u ).
	sttys eol ^c  ( default undef in my settings )
	sttys eol2 ^d  ( default undef in my settings )
*/
