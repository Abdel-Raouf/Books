/* shows how terminals can be set in raw mode.
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
	int i,j;
	struct termios oldset,newset;
	tcgetattr(0,&oldset);
	cfmakeraw(&newset); // different flags will be set.
	newset.c_cc[VTIME] = 0;
	newset.c_cc[VMIN] = 10; // wait till 10 chars are available.
	tcsetattr(0,TCSANOW,&newset);	
	i = read(0,buf,100);
	for(j=0;j<i;j++)
		printf(" %d\n",buf[j]);
	tcsetattr(0,TCSANOW,&oldset);	
}






