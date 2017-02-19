#include<stdio.h>
#include <sys/stat.h>
struct stat st;
 
struct Student {
   int roll;
   char name[12];
   int percent;
} s1 = { 10, "SMJC", 80 };
 
int main() {
   FILE *fp;
   struct Student s2;
 
   //Write details of s1 to file
   fp = fopen("C:\\project\\books\\java-Preparation\\main_docs\\student_enroll.txt", "w");
   fwrite(&s1, sizeof(s1), 1, fp);
   fclose(fp);

   stat("C:\\project\\books\\java-Preparation\\main_docs\\student_enroll.txt", &st);
   printf("file size is: %d bytes",st.st_size);
   
   fp = fopen("ip.txt", "r");
   fread(&s2, sizeof(s2), 1, fp);
   fclose(fp);

   stat("C:\\project\\books\\java-Preparation\\main_docs\\student_enroll.txt", &st);
   printf("file size is: %d bytes",st.st_size);
   
   
	
   printf("\nRoll : %d", s2.roll);
   printf("\nName : %s", s2.name);
   printf("\nPercent : %d", s2.percent);
 
   return (0);
}