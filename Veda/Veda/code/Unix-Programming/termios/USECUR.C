
/* A simple example demonstrating how to develop application using curses
Version : 1.0
Author : Team -C
*/

# include <stdio.h>
# include <curses.h>

main(){
	WINDOW *win;
	chtype car1='n'|A_UNDERLINE|A_REVERSE|A_BLINK;
	chtype car2='a'|A_UNDERLINE|A_REVERSE|A_BLINK;
	chtype car3='m'|A_UNDERLINE|A_REVERSE|A_BLINK;
	chtype car4='e'|A_UNDERLINE|A_REVERSE|A_BLINK;
	win = initscr();
	wmove(win,20,20);
	waddch(win,car1);
	waddch(win,car2);
	waddch(win,car3);
	waddch(win,car4);
	wrefresh(win);
	getchar();
	getchar();
	endwin();
}
