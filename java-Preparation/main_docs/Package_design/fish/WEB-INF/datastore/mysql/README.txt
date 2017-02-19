Contains the CREATE TABLE statements and related items for 
creating and populating a target MySQL database. 

The example scripts supplied here are for MySQL 5.5.8.

For an example configuration of a connection pool using Tomcat,
please see the Getting Started Guide :
http://www.web4j.com/GettingStartedGuide.jsp

One can use any relational database with WEB4J.
However, when getting starting with the WEB4J example apps, 
you are strongly encouraged to use MySQL.

Porting this script to other databases should *usually* not involve changes to 
source code. Instead, the following configuration changes are
needed :
- some settings in web.xml
- configuration of a DataSource in the web container
- reviewing the .sql file(s)
  As an example of the changes required in .sql files, the 
  following differences were found when a port of one app from MySQL 
  to Oracle was performed :
      FORMAT -> TO_CHAR
      DATEFORMAT -> TO_DATE
      CONCAT(x,y,z) -> ||
      'AS' aliases need quoting to preserve case

