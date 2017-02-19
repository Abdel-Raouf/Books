  
/* A simple example demonstrating how to develop application using curses
Version : 1.0
Author : Team -C
*/

# include <stdio.h>
# include <curses.h>

main(){
	WINDOW *win;
	char name[30];
	char *data = "NAME";
	win = initscr();
	wattron(win,A_REVERSE);
	wmove(win,10,10);
	waddstr(win,data);
	wrefresh(win);
	wattroff(win,A_REVERSE);
	wgetnstr(win,name,30);
	printf(" you have entered %s\n",name);
	getchar();
	getchar();
	endwin();
}
