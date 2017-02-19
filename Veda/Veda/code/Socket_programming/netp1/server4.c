/* Example showing how to implement tcp/ip server.
Version: 1.0
Author : Team -C
*/
# include <unistd.h>
# include <sys/socket.h>
# include <sys/types.h>
# include <string.h>
#include <netinet/in.h>

#define CHILDPROCESS 10

main(){
	int listfd,connfd,retval;
	socklen_t clilen;
	struct sockaddr_in cliaddr, servaddr;
	
	char buf[25];
	int nread =0;
	pid_t childpid;
	
	listfd = socket(AF_INET, SOCK_STREAM, 0);
	if (listfd < 0){
		perror("sock:");
		exit(1);
	}
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port = htons(8003);

	retval = bind(listfd, (struct sockaddr *) &servaddr, sizeof(servaddr));
	if(retval < 0){
		perror("bind:");
		exit(2);
	}

	listen(listfd, 5);
	
	for(int count = 0, count < CHILDPROCESS; count++){
		if((childpid = fork()) == 0){
			while(1){
				connfd = accept(listfd, NULL, NULL);
				close(listfd);
				printf(" client connected \n");
				nread = read(connfd,buf,200);
				buf[n]='\0';
				printf(" data rec'd from client = %s\n",buf);
				printf(" requested handled by server %d\n",getpid());
				write(connfd,"Good Bye",8);
				close(connfd);
				exit(0); // terminate child.
			}
		}
	}
}


