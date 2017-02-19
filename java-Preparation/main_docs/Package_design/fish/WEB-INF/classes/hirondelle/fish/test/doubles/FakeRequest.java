package hirondelle.fish.test.doubles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.text.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.util.Args;
import hirondelle.web4j.util.Util;
import static hirondelle.web4j.util.Consts.EMPTY_STRING;

/**
 Fake implementation of {@link HttpServletRequest}, used 
 only for testing outside of the regular runtime environment.
 
 <P>Various methods have been added to make testing more convenient.
 
 <P>The {@link #logInAuthenticatedUser(String, List)} method allows you 
 to mimic an authenticated user.
*/
public final class FakeRequest implements HttpServletRequest {

  /** Used by factory methods to distinguish between <tt>GET</tt> and <tt>POST</tt> requests.*/
  public enum HttpMethod {GET, POST}

  /** Factory method for <tt>GET</tt> requests . */
  public static FakeRequest forGET(String aServletMatchingPath, String aQueryString){
    return new FakeRequest (aServletMatchingPath, EMPTY_STRING, aQueryString, HttpMethod.GET);
  }
  
  /** Factory method for <tt>POST</tt> requests . */
  public static FakeRequest forPOST(String aServletMatchingPath, String aQueryString){
    return new FakeRequest (aServletMatchingPath, EMPTY_STRING, aQueryString, HttpMethod.POST);
  }
  
  /** Full constructor. */
  public FakeRequest(
    String aScheme, String aServerName, Integer aServerPort, String aContextPath, 
    String aServletMatchingPath, String aExtraPath, String aQueryString, HttpMethod aMethod
  ){
    fScheme = aScheme;
    fServerName = aServerName;
    fServerPort = aServerPort;
    fContextPath = ensureNonNullStartsWithSlash(aContextPath);
    fServletMatchingPath = ensureNonNullStartsWithSlash(aServletMatchingPath);
    fExtraPath = ensureNonNullStartsWithSlash(aExtraPath);
    extractParamsFrom(aQueryString);
    fMethod = aMethod;
  }
  
  /*
   Methods added for testing. 
  */
  
  /** Method added for testing. */ 
  public void setContentType(String aContentType){ fContentType = aContentType; }
  /** Method added for testing. */ 
  public void setContentLength(int aContentLength){  fContentLength = aContentLength;  }
  /** Method added for testing. */ 
  public void setProtocol(String aProtocol){  fProtocol = aProtocol;  }
  /** Method added for testing. */ 
  public void setScheme(String aScheme){ fScheme = aScheme;  }
  /** Method added for testing. */ 
  public void setServerName(String aServerName){ fServerName = aServerName;  }
  /** Method added for testing. */ 
  public void setServerPort(int aServerPort){ fServerPort = aServerPort;  }
  /** Method added for testing. */ 
  public void setRemoteAddr(String aRemoteAddr){ fRemoteAddr = aRemoteAddr;  }
  /** Method added for testing. */ 
  public void setRemoteHost(String aRemoteHost){ fRemoteHost = aRemoteHost;  }
  /** Method added for testing. */ 
  public void setIsSecure(boolean aIsSecure){ fIsSecure = aIsSecure;  }
  /** Method added for testing. */ 
  public void setRemotePort(int aRemotePort){ fRemotePort = aRemotePort;  }
  /** Method added for testing. */ 
  public void setLocale(Locale aLocale){ fLocale = aLocale; }

  /** Method added for testing. */ 
  public void addCookie(String aName, String aValue){
    Args.checkForContent(aName);
    fCookies.put(aName, aValue);
  }

  /** Method added for testing. */ 
  public void addParameter(String aName, String aValue){
    Args.checkForContent(aName);
    List<String> existingValues = fParams.get(aName);
    if ( existingValues != null ) {
      existingValues.add(aValue);
    }
    else {
      List<String> newValues = new ArrayList<String>();
      newValues.add(aValue);
      fParams.put(aName, newValues);
    }
  }
  
  /** 
   Date/time headers must use RFC 1123 format. 
    Method added for testing. 
  */
  public void addHeader(String aName, String aValue){
    Args.checkForContent(aName);
    if( fHeaders.containsKey(aName) ){
      List<String> values = fHeaders.get(aName);
      values.add(aValue);
    }
    else {
      List<String> values = new ArrayList<String>();
      values.add(aValue);
      fHeaders.put(aName, values);
    }
  }
  
  /** Method added for testing. */ 
  public void logInAuthenticatedUser(String aUserName, List<String> aRoles){
    Args.checkForContent(aUserName);
    fUserName = aUserName;
    fUserRoles.addAll(aRoles);
  }
  
  /** Method added for testing. */ 
  public void setRequestedSessionId(String aSessionId){  fRequestedSessionId = aSessionId;  }
  
  /*
   ServletRequest methods. 
  */ 
  
  public Object getAttribute(String aName) {
    return fAttrs.get(aName);
  }

  public Enumeration getAttributeNames() {
    return Collections.enumeration(fAttrs.keySet());
  }

  public void setAttribute(String aName, Object aObject) {
    fAttrs.put(aName, aObject);
  }

  public void removeAttribute(String aName) {
    fAttrs.remove(aName);
  }

  /** Default value 'UTF-8'. */
  public String getCharacterEncoding() {  
    return fCharEncoding;  
  }

  public void setCharacterEncoding(String aEncoding) throws UnsupportedEncodingException {
    fCharEncoding = aEncoding;
  }

  /** Default value 0.  */
  public int getContentLength() {
    return fContentLength;
  }

  /** Default value 'text/html'.  */
  public String getContentType() {
    return fContentType;
  }

  public String getParameter(String aName) {
    Args.checkForContent(aName);
    String result = null;
    List<String> existingValues = fParams.get(aName);
    if ( existingValues != null ) {
      result = existingValues.get(FIRST);
    }
    return result;
  }

  public Enumeration getParameterNames() {
    return Collections.enumeration(fParams.keySet());
  }

  public String[] getParameterValues(String aName) {
    Args.checkForContent(aName);
    String[] result = null;
    List<String> existingValues = fParams.get(aName);
    if ( existingValues != null ) {
      result = existingValues.toArray(new String[0]);
    }
    return result;
  }

  public Map getParameterMap() {
    Map<String, String[]> result = new LinkedHashMap<String, String[]>();
    for(String name: fParams.keySet()) {
      String[] values = fParams.get(name).toArray(new String[0]);
      result.put(name, values);
    }
    return result;
  }

  public String getProtocol() {
    return fProtocol;
  }

  /** Default value 'http'.  */
  public String getScheme() {
    return fScheme;
  }

  /** Default value 'Test Double'.  */
  public String getServerName() {  return fServerName; }
  /** Default value 8080.  */
  public int getServerPort() {  return fServerPort;  }

  /** Default value '127.0.0.1'.  */
  public String getLocalAddr() { return fLocalAddr; }
  /** Default value '127.0.0.1'.  */
  public String getLocalName() {  return fLocalName; }
  /** Default value 8080.  */
  public int getLocalPort() {  return fLocalPort; }
  
  /** Default value '127.0.0.1'.  */
  public String getRemoteAddr() {  return fRemoteAddr; }
  /** Default value '127.0.0.1'.  */
  public String getRemoteHost() {  return fRemoteHost; }
  /** Default value '80'.  */
  public int getRemotePort() { return fRemotePort;  }


  /** Default value <tt>Locale.ENGLISH</tt>. */
  public Locale getLocale() {
    return fLocale;
  }
  /** Returns only one Locale. */
  public Enumeration getLocales() {
    Collection<Locale> locales = new ArrayList<Locale>();
    locales.add(fLocale);
    return Collections.enumeration(locales);
  }

  public boolean isSecure() { return fIsSecure; }

  /** Returns <tt>null</tt> - not implemented. */
  public RequestDispatcher getRequestDispatcher(String aPath) { return null; }
  /** Returns <tt>null</tt> - deprecated. */
  public String getRealPath(String aPath) { return null; }
  /** Returns <tt>null</tt> - not implemented. */
  public ServletInputStream getInputStream() throws IOException {  return null;  }
  /** Returns <tt>null</tt> - not implemented. */
  public BufferedReader getReader() throws IOException {  return null; }

  /*
   HttpServletRequest methods. 
  */

  /** Returns <tt>null</tt> - not authenticated. */
  public String getAuthType() { return null; }

  public Cookie[] getCookies() {
    List<Cookie> cookies = new ArrayList<Cookie>();
    if ( ! fCookies.isEmpty() ) {
      for(String name: fCookies.keySet()){
        String value = fCookies.get(name);
        Cookie cookie = new Cookie(name, value);
        cookies.add(cookie);
      }
    }
    return cookies.isEmpty() ? null : cookies.toArray(new Cookie[0]);
  }

  public long getDateHeader(String aName) {
    Args.checkForContent(aName);
    long result = -1;
    String value = getHeader(aName);
    if(value != null) {
      SimpleDateFormat format = new SimpleDateFormat(PATTERN_RFC1123);
      try {
        Date date = format.parse(value);
        result = date.getTime();
      }
      catch (ParseException ex){
        throw new IllegalArgumentException("Cannot parse date/time header value using RFC 1123: " + Util.quote(value));
      }
    }
    return result;
  }

  public String getHeader(String aName) {
    Args.checkForContent(aName);
    String result = null;
    if( fHeaders.containsKey(aName) ) {
      result = fHeaders.get(aName).get(FIRST); 
    }
    return result;
  }

  public Enumeration getHeaders(String aName) {
    Args.checkForContent(aName);
    Collection<String> result = new ArrayList<String>();
    List<String> values = fHeaders.get(aName);
    if( values != null ) {
      result.addAll(values);
    }
    return Collections.enumeration(result);
  }

  public Enumeration getHeaderNames() {
    Collection<String> result = new ArrayList<String>();
    for(String name : fHeaders.keySet() ){
      result.add(name);
    }
    return Collections.enumeration(result);
  }

  public int getIntHeader(String aName) {
    int result = -1;
    String value = getHeader(aName);
    if(value != null) {
      result = Integer.valueOf(value);
    }
    return result;
  }

  public String getMethod() {
    return fMethod.toString();
  }

  public String getPathInfo() {
    return fExtraPath;
  }

  /** Returns <tt>null</tt> - not implemented. */
  public String getPathTranslated() {  return null; }

  public String getContextPath() {
    return fContextPath;
  }

  /**
   Created from the given request parameters.
   Includes the initial '?'. Returns <tt>null</tt> if no parameters present.
   <i>This implementation is artificial but convenient, since it makes no distinction 
   between GET and POST</i>.
  */
  public String getQueryString() {
    StringBuilder result = new StringBuilder("");
    boolean hasAddedFirstParam = false;
    for (String name : fParams.keySet()){
      if ( ! hasAddedFirstParam ) {
        result.append("?");
        hasAddedFirstParam = true;
      }
      else {
        result.append("&");
      }
      result.append(name + "=" + fParams.get(name));
    }
    return hasAddedFirstParam ? result.toString() : null;
  }

  public String getRemoteUser() {
    return fUserName;
  }

  public boolean isUserInRole(String aRole) {
    return fUserRoles.contains(aRole);
  }

  public Principal getUserPrincipal() {
    return new FakePrincipal(fUserName);
  }

  public String getRequestURI() {
    return fContextPath + fServletMatchingPath + fExtraPath;
  }

  public StringBuffer getRequestURL() {
    String result = fScheme + "://" + fServerName;
    if ( fServerPort != null ) {
      result = result + ":" + fServerPort.toString();
    }
    result = result + fContextPath + fServletMatchingPath + fExtraPath + getQueryString();
    return new StringBuffer(result);
  }

  public String getServletPath() {
    return fServletMatchingPath;
  }

  public String getRequestedSessionId() {
    return fRequestedSessionId; 
  }

  public boolean isRequestedSessionIdValid() {
    return Util.textHasContent(fRequestedSessionId) && fRequestedSessionId.equals(getSession(false).getId());
  }

  /** Hard-code to <tt>true</tt>. */
  public boolean isRequestedSessionIdFromCookie() {
    return true;
  }

  /** Hard-code to <tt>false</tt>. */
  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  /** Hard-code to <tt>false</tt>. */
  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }

  public HttpSession getSession(boolean aCreateNew) {
    if( fSession == null ) {
      fSession = FakeSession.joinOrCreate(fRequestedSessionId, aCreateNew);
    }
    return fSession;
  }

  public HttpSession getSession() {
    if( fSession == null ) {
      fSession = FakeSession.joinOrCreate(fRequestedSessionId, true);
    }
    return fSession;
  }

  // PRIVATE //
  private Map<String, List<String>> fParams = new LinkedHashMap<String, List<String>>(); 
  private static final int FIRST = 0;
  private Map<String, Object> fAttrs = new LinkedHashMap<String, Object>();
  
  //Items for ServletRequest
  private String fCharEncoding = "UTF-8";
  private String fContentType = "text/html";
  private int fContentLength;
  private String fProtocol = "HTTP/1.1";
  private String fScheme = "http";
  
  private String fServerName = "Test Double";
  private Integer fServerPort = 8080;
  
  private String fLocalName = "Test Double";
  private int fLocalPort = 8080;
  private String fLocalAddr = "127.0.0.1";
  
  private String fRemoteAddr = "127.0.0.1";
  private String fRemoteHost = "127.0.0.1";
  private int fRemotePort = 80;
  
  private Locale fLocale = Locale.ENGLISH;
  
  private boolean fIsSecure = false;

  //Items for HttpServletRequest
  private String fRequestedSessionId = EMPTY_STRING;
  private Map<String, String> fCookies = new LinkedHashMap<String, String>();
  private Map<String, List<String>> fHeaders = new LinkedHashMap<String, List<String>>();
  private static final String PATTERN_RFC1123 =  "EEE, dd MMM yyyy HH:mm:ss zzz";
  private static final String SLASH = "/";
  private HttpMethod fMethod;
  private String fContextPath = EMPTY_STRING;
  private String fServletMatchingPath = EMPTY_STRING;
  private String fExtraPath = EMPTY_STRING;
  private String fUserName = EMPTY_STRING;
  private List<String> fUserRoles = new ArrayList<String>();
  private HttpSession fSession;

  private FakeRequest(String aServletMatchingPath, String aExtraPath, String aQueryString, HttpMethod aMethod){
    fServletMatchingPath = ensureNonNullStartsWithSlash(aServletMatchingPath);
    fExtraPath = ensureNonNullStartsWithSlash(aExtraPath);
    extractParamsFrom(aQueryString);
    fMethod = aMethod;
  }
  
  private String ensureNonNullStartsWithSlash(String aText){
    String result = aText;
    if ( Util.textHasContent(aText) ) {
      if (! aText.startsWith(SLASH)){
        throw new IllegalArgumentException("Does not start with a '/' character: " + Util.quote(aText));
      }
    }
    else {
      result = EMPTY_STRING;
    }
    return result;
  }
  
  /**
   @param aQueryString 'blah=yes', 'blah=', 'blah=yes&Id=123', 'blah=yes&Id='. 
  */
  private void extractParamsFrom(String aQueryString){
    if( Util.textHasContent(aQueryString)) {
      StringTokenizer firstParse = new StringTokenizer(aQueryString, "&");
      while ( firstParse.hasMoreElements() ) {
        String eachNameValuePair = firstParse.nextToken();
        StringTokenizer secondParse = new StringTokenizer(eachNameValuePair, "=");
        //sometimes the value is missing. in that case, coerce the value into an empty string
        List<String> items = new ArrayList<String>();
        while ( secondParse.hasMoreTokens() ) {
          items.add(secondParse.nextToken());
        }
        String name = items.get(0); //assume name always present
        String value = EMPTY_STRING; //value may not be present
        if( items.size() > 1 ) { 
          value = items.get(1); //value is present
        }
        addParameter(name, value);
      }
    }
  }
  
  private static class FakePrincipal implements Principal {
    FakePrincipal(String aName){
      fName = aName;
    }
    public String getName() {
      return fName;
    }
    @Override public String toString(){
      return fName;
    }
    @Override public boolean equals(Object aThat){
      Boolean result = ModelUtil.quickEquals(this, aThat);
      if ( result == null ){
        FakePrincipal that = (FakePrincipal) aThat;
        result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
      }
      return result;    
    }
    @Override public int hashCode() {
      return ModelUtil.hashCodeFor(getSignificantFields());
    }
    private String fName;
    private Object[] getSignificantFields() { return new Object[] {fName}; }
  }
}
