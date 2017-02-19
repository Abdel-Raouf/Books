package hirondelle.fish.test.doubles;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.sql.DriverManager;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.ConnectionSource;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.util.Util;

/**
 Implementation of {@link ConnectionSource} suitable for testing.
 
 <P>The 'real' implementation uses JNDI lookup of items from a connection pool configured 
 in the container. This implementation does not use JNDI. Instead, it simply creates a connection 
 using <tt>DriverManager</tt>, each time one is needed. Other implementations are possible.
 
 <P>The mechanism for replacing the real <tt>ConnectionSource</tt> with this class uses an
 <tt>init-param</tt> in {@link FakeServletConfig}.
*/
public final class FakeConnectionSrc extends ConnectionSrc {

  /** Fetch a connection using <tt>DriverManager</tt>. */
  protected Connection getConnectionByName(String aDbName) throws DAOException {
    if ( ! getDatabaseNames().contains(aDbName)) {
      throw new IllegalArgumentException("Unknown database name : " + Util.quote(aDbName));      
    }
    
    /*
     WARNING: HARD-CODED CREDENTIALS! 
     You may prefer to use System properties for these items, or 
     some other technique.
    */
    String USER_NAME = "myname";
    String PASSWORD = "mypassword";
    
    String DB_CONN_STRING = getConnectionString(aDbName);
    String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver"; 
    
    Connection result = null;
    try {
       Class.forName(DRIVER_CLASS_NAME).newInstance();
    }
    catch (Exception ex){
       fLogger.severe("Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME);
    }

    //get the connection from DriverManager
    try {
      result = DriverManager.getConnection(DB_CONN_STRING , USER_NAME, PASSWORD);
    }
    catch (SQLException e){
       fLogger.severe( "Driver loaded, but cannot connect to db: " + DB_CONN_STRING);
    }
    return result;    
  }
  
  // PRIVATE //
  private static final Logger fLogger = Util.getLogger(FakeConnectionSrc.class);
}
