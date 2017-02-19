/* 
	header showing the details of our own protocol header 
	myproto.h
*/

# define IPPROTO_MYPROTO 100

struct myphdr{
	short source_app;
	short dest_app;
	short data_len;
};
