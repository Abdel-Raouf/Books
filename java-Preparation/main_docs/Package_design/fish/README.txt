Fish And Chips Club - the WEB4J example application.
Version : 4.10.0.0
Built with web4j.jar version : ${web4j.version}
Build Date : 2014-10-30 13:59:51

*** FOR GETTING STARTED, SEE: ************
http://www.web4j.com/GettingStartedGuide.jsp
**************************************


-- THE hirondelle.web4j.config PACKAGE ------------
You should only edit these classes *after* you have successfully started and 
exercised the application. Initially, you should not edit or delete these classes.


-- LOG FILES ------------------------------------
If you are having problems getting started, please consult your log files. 

There are two kinds of log files : 
  - application log file (configured in web.xml)
  - server logs (Note that Tomcat has several log files, not one.)
  
 Both kinds of logs contain useful information. It is important to check 
 *both* when troubleshooting. 
 
The EXAMPLE_LOG_FILE_.txt file is included with the Fish and Chips Club 
as an example of what an application log file looks like when startup 
completes successfully. (It uses logging level FINE.)

If you are still having a problem getting started, please send an email to 
web4j-users@googlegroups.com or support@web4j.com. The email should include:
 - a description of the problem
 - application log files
 - all server log files that have content files 
   (Tomcat has several log files under [TOMCAT_HOME]/logs)


-- REQUIRED JARS --------------------------------------
The following jars are included in fish.zip :
- web4j.jar 
- a database and java database driver :
  MySQL 5.5.8 was used in development. 
  To port to another database, translate the CREATE TABLE statements, and review the .sql files.
- an implementation of JSP Standard Template Library (JSTL) :
  jstl.jar and standard.jar from Jakarta
- two items for sending emails :
   - mail.jar JavaMail API
   - activation.jar JavaBeans Activation jar
   (alternatively, the large j2ee.jar can take the place of both of these)
- Two Apache tools are used for implementing file upload 
    Apache Commons File Upload: commons-fileupload-1.2.1.jar
    Apache Commons IO: commons-io-1.4.jar

Other required jars : 
- JUnit for unit testing (needed at runtime, if test classes included in build)
- Ant for build scripts

