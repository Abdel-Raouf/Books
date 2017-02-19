package hirondelle.fish.webmaster.diagnostics; 

import hirondelle.web4j.BuildImpl;
import hirondelle.web4j.Controller;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.ConnectionSource;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Consts;
import hirondelle.web4j.util.Stopwatch;
import hirondelle.web4j.util.Util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 Show an extensive listing of diagnostic information, useful for solving problems.
 
 @view view.jsp
*/
public final class ShowDiagnostics extends ActionImpl {
  
  /** Constructor. */
  public ShowDiagnostics(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** 
   Retrieve diagnostic information and display it to the user.
   
   <P>The diagnostic information includes :
   <ul>
   <li>up time
   <li>logging configuration
   <li>jar versions
   <li>system properties
   <li>context init params
   <li>server information
   <li>application scope items
   <li>session scope items
   <li>request information
   <li>request headers
   <li>request cookies
   <li>response encoding
   <li>database names and versions
   <li>database URIs
   </ul> 
  */
  public ResponsePage execute() throws AppException {
    placeDiagnosticDataInScope(getRequestParser().getRequest(), getRequestParser().getResponse());
    return getResponsePage();
  }
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage(
    "Diagnostics", "view.jsp", ShowDiagnostics.class
  );
  private static final String SPECIFICATION_TITLE = "Specification-Title";
  private static final String SPECIFICATION_VERSION = "Specification-Version";
  private static final String UNSPECIFIED = "Unspecified in Manifest";
  private static final Logger fLogger = Util.getLogger(ShowDiagnostics.class);
  
  private void placeDiagnosticDataInScope(HttpServletRequest aRequest, HttpServletResponse aResponse) throws DAOException {
    Stopwatch stopwatch = new Stopwatch();
    stopwatch.start();
    
    fLogger.fine("Adding system properties.");
    addToRequest("systemProperties", sortMap(System.getProperties()));
    fLogger.fine("Adding Context init-params.");
    addToRequest("contextInitParams", sortMap(getContextInitParams(aRequest)));
    fLogger.fine("Adding application scope items.");
    addToRequest("appScopeItems", getAppScope(aRequest));
    fLogger.fine("Adding session scope items.");
    addToRequest("sessionScopeItems", getSessionScope(aRequest));
    fLogger.fine("Adding container/servlet info.");
    addToRequest("containerServletInfo", getContainerServletInfo(aRequest));
    fLogger.fine("Adding request info.");
    addToRequest("requestInfo", getRequestInfo(aRequest));
    fLogger.fine("Adding database information.");
    addToRequest("dbInfo", getDbInfo());
    fLogger.fine("Adding loggers.");
    addToRequest("loggers", getLoggers());
    fLogger.fine("Adding Controller name/version");
    addToRequest("controller_name_version", Controller.WEB4J_VERSION);
    fLogger.fine("Adding JAR versions.");
    addToRequest("jarVersions", getJarVersions(aRequest));
    fLogger.fine("Adding uptime.");
    addToRequest("uptime", getUptime(aRequest));
    fLogger.fine("Adding request headers.");
    addToRequest("headers", getHeaders(aRequest));
    addToRequest("responseEncoding", getResponseEncoding(aResponse));
    fLogger.fine("Adding cookies.");
    addToRequest("cookies", getCookies(aRequest));
    
    fLogger.fine("Finished retrieving data.");
    stopwatch.stop();
    addToRequest("stopwatch", stopwatch.toString());
  }
  
  private ServletContext getContext(HttpServletRequest aRequest){
    return getExistingSession().getServletContext();
  }
  
  private Map sortMap(Map aInput){
    Map result = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    result.putAll(aInput);
    return result;
  }
  
  private Map<Object, Object> getAppScope(HttpServletRequest aRequest){
    Map<Object, Object> result = new HashMap<Object, Object>();
    Enumeration keys = getContext(aRequest).getAttributeNames();
    while ( keys.hasMoreElements() ) {
      Object key = keys.nextElement();
      Object value = getContext(aRequest).getAttribute(key.toString());
      result.put(key, value);
    }
    return sortMap(result);
  }
  
  
  private Map<Object, Object> getSessionScope(HttpServletRequest aRequest){
    Map<Object, Object> result = new HashMap<Object, Object>();
    Enumeration keys = getExistingSession().getAttributeNames();
    while ( keys.hasMoreElements() ) {
      Object key = keys.nextElement();
      Object value = getExistingSession().getAttribute(key.toString());
      result.put(key, value);
    }
    return sortMap(result);
  }
  
  private Map<String, String> getContainerServletInfo(HttpServletRequest aRequest){
    Map<String, String> result = new LinkedHashMap<String, String>();
    ServletContext context = getContext(aRequest);
    result.put("Host Name", aRequest.getServerName());
    result.put("Operating System", System.getProperty("os.arch") + " " +  System.getProperty("os.name") + " " + System.getProperty("os.version"));
    result.put("Container", context.getServerInfo());
    result.put("Controller Name", context.getClass().getName());
    result.put("Home URL", getHome(aRequest));
    return result;
  }
  
  private Map<String, Object> getRequestInfo(HttpServletRequest aRequest){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("Server Port", new Integer(aRequest.getServerPort()));
    result.put("Client IP", aRequest.getRemoteAddr());
    result.put("Character Encoding", aRequest.getCharacterEncoding());
    result.put("Protocol", aRequest.getProtocol());
    result.put("Server Name", aRequest.getServerName());
    result.put("Content Length", new Integer(aRequest.getContentLength()));
    return sortMap(result);
  }

  private Map<String, Map<String, String>> getDbInfo() throws DAOException {
    Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
    ConnectionSource connSource = BuildImpl.forConnectionSource();
    for(String dbName : connSource.getDatabaseNames() ){
      result.put(dbName, getDbInfo(dbName, connSource));
    }
    return result;
  }
  
  private Map<String, String> getDbInfo(String aDbName, ConnectionSource aConnSource) throws DAOException {
    Map<String, String> result = new HashMap<String, String>();
    Connection connection  = null;
    try {
      connection = aConnSource.getConnection(aDbName);
      DatabaseMetaData metaData = connection.getMetaData();
      addDatabaseURIs(result, metaData);
      addNamesAndVersions(result, metaData);
      addDriverNamesAndVersions(result, metaData);
    }
    catch (SQLException ex){
      throw new DAOException("Cannot access database metadata.", ex);
    }
    finally {
      close(connection);
    }
    return result;
  }
  
  private void addNamesAndVersions(Map<String, String> aResult, DatabaseMetaData aMetaData) throws SQLException {
    StringBuilder line = new StringBuilder();
    line.append(aMetaData.getDatabaseProductName() + Consts.SPACE); 
    line.append(aMetaData.getDatabaseProductVersion() + Consts.SPACE); 
    line.append(aMetaData.getDatabaseMajorVersion() + Consts.SPACE);
    line.append(aMetaData.getDatabaseMinorVersion());
    aResult.put("Database Version", line.toString());
  }
  
  private void addDriverNamesAndVersions(Map<String, String> aResult, DatabaseMetaData aMetaData) throws SQLException {
    StringBuilder line = new StringBuilder();
    line.append("(JDBC Version ");
    line.append(aMetaData.getJDBCMajorVersion() + "."); 
    line.append(aMetaData.getJDBCMinorVersion() + Consts.SPACE); 
    line.append(") ");
    line.append(aMetaData.getDriverName() + Consts.SPACE); 
    line.append(aMetaData.getDriverVersion() + Consts.SPACE);
    aResult.put("Database Driver Version", line.toString());
  }
  
  private void addDatabaseURIs(Map<String, String> aResult, DatabaseMetaData aMetaData) throws SQLException {
    StringBuilder line = new StringBuilder();
    line.append(aMetaData.getURL());
    aResult.put("Database URI", line.toString());
  }
  
  private String getHome(HttpServletRequest aRequest) {
    String url =  aRequest.getRequestURL().toString();
    String contextPath = aRequest.getContextPath();
    fLogger.fine( url );
    fLogger.fine( contextPath );
    int endIndex = url.indexOf(contextPath) + contextPath.length();
    return url.substring(0, endIndex);
  }
  
  private Map<String, Level> getLoggers() {
    Map<String, Level> result = new HashMap<String, Level>();
    LogManager logManager = LogManager.getLogManager();
    Enumeration loggerNames = logManager.getLoggerNames();
    while ( loggerNames.hasMoreElements() ){
      String loggerName = (String)loggerNames.nextElement();
      Logger logger = logManager.getLogger(loggerName);
      result.put(loggerName, logger.getLevel());
    }
    return sortMap(result);
  }
  
  /**
   Get all .jar files in <tt>/WEB-INF/lib/</tt> directory, scan the manifest for its 
   <tt>Specification-Version</tt>, and report it along with the file name. 
   
   <P>Some jars will not have a value for <tt>Specification-Version</tt>. These will be 
   reported as {@link #UNSPECIFIED}.
  */  
  private Map<String, String> getJarVersions(HttpServletRequest aRequest){
    Map<String, String> result = new HashMap<String, String>();
    ServletContext context = getContext(aRequest); 
    Set paths = context.getResourcePaths("/WEB-INF/lib/");
    fLogger.fine(paths.toString());
    Iterator iter = paths.iterator();
    while ( iter.hasNext() ) {
      String jarFileName = iter.next().toString();
      JarInputStream jarStream = null;
      try {
        jarStream = new JarInputStream(context.getResourceAsStream(jarFileName));
      }
      catch (IOException ex){
        fLogger.severe("Cannot open jar file (to fetch Specification-Version from the jar Manifest).");
      }
      result.put(jarFileName, fetchSpecNamesAndVersions(jarStream));
    }
    return sortMap(result);
  }

  /** 
   A Jar can have more than one entry for spec name and version. 
   For example, the servlet jar implements both servlet spec and jsp spec.
   
   <P>Search for all attributes named Specification-Title and Specification-Version, 
   and simply return them in the order they appear.
   
   <P>If none of these appear, simply return an empty String.
  */
  private String fetchSpecNamesAndVersions(JarInputStream aJarStream) {
    StringBuffer result = new StringBuffer();
    Manifest manifest = aJarStream.getManifest();
    if ( manifest != null ){
      Attributes attrs = (Attributes)manifest.getMainAttributes();
      for (Iterator iter = attrs.keySet().iterator(); iter.hasNext(); ) {
        Attributes.Name attrName = (Attributes.Name)iter.next();
        addSpecAttrIfPresent(SPECIFICATION_TITLE, result, attrs, attrName);
        addSpecAttrIfPresent(SPECIFICATION_VERSION, result, attrs, attrName);
      }
      fLogger.fine("Specification-Version: " + result);
    }
    else {
      fLogger.fine("No manifest.");
    }
    return result.toString();
  }

  private void addSpecAttrIfPresent(String aName, StringBuffer aResult, Attributes aAttrs, Attributes.Name aAttrName) {
    if ( aName.equalsIgnoreCase(aAttrName.toString()) ) {
      if ( Util.textHasContent(aAttrs.getValue(aAttrName)) ){
        aResult.append(aAttrs.getValue(aAttrName) + " ");
      }
    }
  }
  
  private Float getUptime(HttpServletRequest aRequest){
    TimeZone tz = (TimeZone)getExistingSession().getServletContext().getAttribute(Controller.TIME_ZONE);
    DateTime now = DateTime.now(tz);
    DateTime startup = getStartTime();
    int SECONDS_PER_DAY = 86400;
    return new Float(startup.numSecondsFrom(now)/SECONDS_PER_DAY);
  }
  
  private DateTime getStartTime(){
    return (DateTime)getExistingSession().getServletContext().getAttribute(Controller.START_TIME);
  }
  
  private Map<String, String> getHeaders(HttpServletRequest aRequest){
    Map<String, String> result = new HashMap<String, String>();
    Enumeration items = aRequest.getHeaderNames();
    while( items.hasMoreElements() ){
      String name = (String)items.nextElement();
      String value = aRequest.getHeader(name);
      result.put(name, value);
    }
    return sortMap(result);
  }

  private Map<String, String> getResponseEncoding(HttpServletResponse aResponse){
    Map<String, String> result = new HashMap<String, String>();
    result.put("Character Encoding", aResponse.getCharacterEncoding());
    return sortMap(result);
  }
  
  private Map<String, String> getCookies(HttpServletRequest aRequest){
    Map<String, String> result = new HashMap<String, String>();
    Cookie[] cookies = aRequest.getCookies();
    if ( cookies != null ){
      List cookieList = Arrays.asList(cookies);
      Iterator iter = cookieList.iterator();
      while ( iter.hasNext() ) {
        Cookie cookie = (Cookie)iter.next();
        result.put(cookie.getName(), "Value:" + cookie.getValue() + ", Comment:" + cookie.getComment() + ", Domain:" + cookie.getDomain() + ", Max-Age:" + cookie.getMaxAge() + ", Path:" + cookie.getPath() + ", Spec- Version:" + cookie.getVersion());
      }
      result = sortMap(result);
    }
    return result;
  }
  
  private Map<String, Object> getContextInitParams(HttpServletRequest aRequest){
    Map<String, Object> result = new HashMap<String, Object>();
    Enumeration initParams = getExistingSession().getServletContext().getInitParameterNames();
    while(initParams.hasMoreElements()) {
      String key = (String)initParams.nextElement();
      Object value = getExistingSession().getServletContext().getInitParameter(key);
      result.put(key, value);
    }
    return result;
  }
  
  private void close(Connection aConnection) throws DAOException {
    try {
      if ( aConnection != null ) {
        fLogger.fine("Closing connection.");
        aConnection.close();
      }
    }
    catch (SQLException ex){
      throw new DAOException("Cannot close connection.", ex);
    }
  }
}
