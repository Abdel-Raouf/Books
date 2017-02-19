/* Example showing how to implement tcp/ip server.
Version: 1.0
Author : Team -C
*/
# include <unistd.h>
# include <sys/socket.h>
# include <sys/types.h>
# include <string.h>
#include <netinet/in.h>

main(){
int listfd,connfd,retval;
socklen_t clilen;
struct sockaddr_in cliaddr, servaddr;

listfd = socket(AF_INET, SOCK_STREAM, 0);
printf("listfd = %d ", listfd);
if (listfd < 0){
	perror("sock:");
	exit(1);
}
bzero(&servaddr, sizeof(servaddr));
servaddr.sin_family = AF_INET;
servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
servaddr.sin_port = htons(8004);

retval = bind(listfd, (struct sockaddr *) &servaddr, sizeof(servaddr));
if(retval < 0){
	perror("bind:");
	exit(2);
}

listen(listfd, 5);

while(1){
	clilen = sizeof(cliaddr);
	connfd = accept(listfd, (struct sockaddr *) &cliaddr, &clilen);
         printf(" connfd= %d",connfd);
	printf(" client connected \n");
	printf(" client's port no = %d\n",htons(cliaddr.sin_port));
	}
}
