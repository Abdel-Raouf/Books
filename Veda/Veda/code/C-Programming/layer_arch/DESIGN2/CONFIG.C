
/* This is a very standard technique used to add multiple types of
handlers
Version : 1.0
Author : Team -C
*/
# include <stdio.h>
# include "handlers.h"

imageHandler *imagehandlers[]={
	&tifhandler,
	&gifhandler,
	NULL // to indicate the end of array
};
