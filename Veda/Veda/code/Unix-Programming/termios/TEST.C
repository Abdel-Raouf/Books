/* shows about controlling terminal.
Version : 1.0
Author : Team -C
*/
# include <termios.h>
# include <unistd.h>
# include <sys/ioctl.h>
# include <stdio.h>

main(){
	getchar();
	getchar();
	ioctl(0,TIOCNOTTY,NULL);
	getchar();
	getchar();
}
