/* shows how terminals settings can be changed.
Version : 1.0
Author : Team -C
*/
# include <termios.h>
# include <unistd.h>
# include <sys/ioctl.h>
# include <stdio.h>
# include <fcntl.h>
# define CTRLCHAR(character) ((character) & 0x1f)

main(){
	char buf[11];
	struct termios oldset,newset;
	tcgetattr(0,&oldset);
	newset = oldset;
	printf(" enter some special chars and see the effect \n");
	read(0,buf,10);
	newset.c_cc[VINTR] = CTRLCHAR('A'); // set interrupt char to A
	newset.c_cc[VKILL] = CTRLCHAR('C'); // set interrupt char to A
	newset.c_cc[VERASE] =CTRLCHAR('B'); 
	tcsetattr(0,TCSANOW,&newset);	
}
/* run the program twice to see the effect, we recommend using stty sane
after testing the programs as we are not resetting the terminal setting to
normal.
*/
