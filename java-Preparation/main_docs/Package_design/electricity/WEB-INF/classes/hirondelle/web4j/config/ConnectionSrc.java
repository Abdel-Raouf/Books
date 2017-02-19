package hirondelle.web4j.config;

import hirondelle.web4j.database.ConnectionSource;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/** 
* Implementation of {@link ConnectionSource}, required by WEB4J.
* 
* <P>There is only one database used in this example application.
* The {@link ConnectionSource} interface is slightly biased towards using 
* more than one database. However, the case of using a single database is still
* straightforward. 
*  
* <P>This implementation uses a <tt>Connection</tt> pool managed by the container.
* This class uses a setting in web.xml which refers to the container's connection pool.
*/
public final class ConnectionSrc implements ConnectionSource  {

  /**  Name used to identify the default database.  */
  public static final String DEFAULT = "DEFAULT";
  
  /** Read in connection strings from <tt>web.xml</tt>. */
  public final void init(Map<String, String> aConfig){
    fDefaultDbConnString = aConfig.get(DEFAULT_CONN_STRING);
    ensureAllSettingsPresent();
    
    fMapNameToConnectionString = new LinkedHashMap<String, String>();
    fMapNameToConnectionString.put(DEFAULT, fDefaultDbConnString);
    fLogger.config("Connection strings : " + Util.logOnePerLine(fMapNameToConnectionString));
  }

  /** Return value contains only {@link #DEFAULT}.  */
  public final Set<String> getDatabaseNames(){
    return Collections.unmodifiableSet(fMapNameToConnectionString.keySet()); 
  }
  
  /** Return a {@link Connection} for the default database.  */
  public final Connection getConnection() throws DAOException {
    return getConnectionByName(DEFAULT);
  }

  /**
  * Return a {@link Connection} for the identified database.
  * (This method doesn't really make much sense when there is only one database to begin with.) 
  * 
  * @param aDatabaseName contains the value {@link #DEFAULT}.
  */
  public final Connection getConnection(String aDatabaseName) throws DAOException {
    return getConnectionByName(aDatabaseName);
  }
  
  // PRIVATE //
  
  /**
  * Maps the database name passed to {@link #getConnection(String)} to the actual connection string.
  */
  private static Map<String, String> fMapNameToConnectionString;
  private static final String DEFAULT_CONN_STRING = "DefaultDbConnectionString";
  private static String fDefaultDbConnString;
  private static final Logger fLogger = Util.getLogger(ConnectionSrc.class);
   
  private static void ensureAllSettingsPresent(){
    if( ! Util.textHasContent(fDefaultDbConnString) ) {
      logError(DEFAULT_CONN_STRING);
    }
  }
   
  private static void logError(String aSettingName){
    fLogger.severe("Web.xml missing init-param setting for " + aSettingName);
  }
  
  private Connection getConnectionByName(String aDbName) throws DAOException {
    Connection result = null;
    String dbConnString = getConnectionString(aDbName);  
    if( ! Util.textHasContent(dbConnString) ){
      throw new IllegalArgumentException("Unknown database name : " + Util.quote(aDbName));      
    }
    try {
      Context initialContext = new InitialContext();
      if ( initialContext == null ) {
        fLogger.severe("DataSource problem. InitialContext is null. Db : " + Util.quote(dbConnString));
      }
      DataSource datasource = (DataSource)initialContext.lookup(dbConnString);
      if ( datasource == null ){
        fLogger.severe("Datasource is null for : " + dbConnString);
      }
      result = datasource.getConnection();
    }
    catch (NamingException ex){
      throw new DAOException("Config error with JNDI and datasource, for db " + Util.quote(dbConnString), ex);
    }
    catch (SQLException ex ){
      throw new DAOException("Cannot get JNDI connection from datasource, for db " + Util.quote(dbConnString), ex);
    }
    return result;
  }
  
  private String getConnectionString(String aDbName){
    return fMapNameToConnectionString.get(aDbName);
  }
}
  