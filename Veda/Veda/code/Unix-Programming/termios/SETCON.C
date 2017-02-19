/* shows how  to set local echo on for control chars.
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
	struct termios oldset,newset;
	int i;
	tcgetattr(0,&oldset);
	newset = oldset;
	newset.c_lflag |= ECHOCTL;
	tcsetattr(0,TCSANOW,&newset);	
	printf(" enter secret code: \n");
	i=read(0,buf,100);
	buf[i]='\0';
	printf("\n you entered %s\n",buf);
	tcsetattr(0,TCSANOW,&oldset);	
}
