#include <scsi/scsi_ioctl.h>
#include <scsi/scsi.h>
#include<fcntl.h>
struct scsi_cmd
{
	int inlen;
	int outlen;
	char data[256];
}CMD;

main()
{
	int fd,cnt;
	memset(CMD.data,0,256);

	fd = open("/dev/sda1",O_RDONLY);
	printf("\n fd = %d",fd);
	CMD.inlen=0;
	CMD.outlen=30;
	CMD.data[0]=0x12;
	CMD.data[5]=1;
	
	cnt = ioctl(fd,SCSI_IOCTL_SEND_COMMAND,(void *)&CMD);
	printf("\n returned  %d",cnt);
	
	for(cnt=1;cnt<40;cnt++)
		printf("%c",CMD.data[cnt]);
	
}
