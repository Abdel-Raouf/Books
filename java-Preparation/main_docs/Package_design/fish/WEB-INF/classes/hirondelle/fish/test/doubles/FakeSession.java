package hirondelle.fish.test.doubles;

import java.io.FileNotFoundException;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionBindingListener;
import hirondelle.web4j.util.Util;
import java.util.logging.*;

/**
 Fake implementation of {@link HttpSession}, used 
 only for testing outside of the regular runtime environment.
*/
public class FakeSession implements HttpSession {
  
  /**
   Each incoming request calls this method in order to join an existing session, or create a new one.
   
   @param aRequestedSessionId identifies the session that the request wishes to join.
   @param aCreateNew if the request fails to join an existing session and <tt>aCreateNew</tt> is true, then 
   a new session is created. If <tt>aCreateNew</tt> is false, then no such new session is created. 
   @return the session which the request has joined, or <tt>null</tt> if no session was joined.
  */
  public static synchronized HttpSession joinOrCreate(String aRequestedSessionId, boolean aCreateNew){
    HttpSession result = tryToJoinExistingSession(aRequestedSessionId);
    if( result == null && aCreateNew ) {
      result = createNewSession();
    }
    if( result != null ) {
      fLogger.fine("Join/Create Session. Id: " + result.getId());
    }
    else {
      fLogger.fine("No session was joined.");
    }
    return result;
  }
  
  /** Returns the time this session object was created. */
  public long getCreationTime() {
    vomitIfInvalidated();
    return fCreationTime;
  }

  /** Return the session id. */
  public String getId() {
    return fSessionId;
  }

  public long getLastAccessedTime() {
    vomitIfInvalidated();
    return fLastAccessTime;
  }

  /** Returns a fake <tt>ServletContext</tt>. */
  public ServletContext getServletContext() {
    return fServletContext;
  }

  public void setMaxInactiveInterval(int aInterval) {
    fMaxInactiveInterval = aInterval;
  }

  /** Default value 30 minutes. */
  public int getMaxInactiveInterval() {
    return fMaxInactiveInterval;
  }

  public Enumeration getAttributeNames() {
    vomitIfInvalidated();
    Hashtable<String, Object> hashTable = new Hashtable<String, Object>(fAttributes);
    return hashTable.keys();
  }

  public Object getAttribute(String aName) {
    vomitIfInvalidated();
    return fAttributes.get(aName);
  }

  public void setAttribute(String aName, Object aObject) {
    vomitIfInvalidated();
    Object existingObject = fAttributes.put(aName, aObject);
    if( aObject instanceof HttpSessionBindingListener) {
      HttpSessionBindingListener listener = (HttpSessionBindingListener)aObject;
      Object value = (existingObject != null ? existingObject : aObject);
      listener.valueBound(new HttpSessionBindingEvent(this, aName, value));
    }
  }

  public void removeAttribute(String aName) {
    vomitIfInvalidated();
    Object existingObject = fAttributes.remove(aName);
    if( existingObject instanceof HttpSessionBindingListener) {
      HttpSessionBindingListener listener = (HttpSessionBindingListener)existingObject;
      listener.valueUnbound(new HttpSessionBindingEvent(this, aName, existingObject));
    }
  }

  public void invalidate() {
    vomitIfInvalidated();
    List<String> attrs = Collections.list(getAttributeNames());
    for(String name: attrs){
      removeAttribute(name);
    }
    fAttributes.clear();
    fHasBeenInvalidated = true;
    //some might remove the session from fSession.
    //for testing, that's likely not very important.
  }

  /** Returns <tt>true</tt> only if no request has yet joined this session.  */
  public boolean isNew() {
    vomitIfInvalidated();
    return fIsPropsective;
  }

  /** Does nothing - deprecated. */
  public void putValue(String aName, Object aValue) { }
  /** Does nothing - deprecated. */
  public void removeValue(String aName) { }
  /** Returns <tt>null</tt> - deprecated. */
  public Object getValue(String aName) {  return null;  }
  /** Returns <tt>null</tt> -  deprecated. */
  public HttpSessionContext getSessionContext() {  return null; }
  /** Returns <tt>null</tt> - deprecated. */
  public String[] getValueNames() {  return null;  }
  
  // PRIVATE //
  private static Map<String, FakeSession> fSessions = new LinkedHashMap<String, FakeSession>();
  private static int fSessionIdGenerator;
  
  /** Not secure. Returns a simple, incremented integer. */
  private static synchronized String getNextSessionId(){
    fSessionIdGenerator++;
    return new Integer(fSessionIdGenerator).toString();
  }
  
  /** Returns <tt>null</tt> if fails to join an existing session.  */
  private static synchronized HttpSession tryToJoinExistingSession(String aSessionId){
    HttpSession result = null;
    FakeSession existingSession = fSessions.get(aSessionId);
    if ( existingSession != null ) {
      existingSession.updateTimes();
      if( existingSession.hasTimedOut() ) {
        existingSession.invalidate();
      }
      else {
        //successfully joined
        existingSession.fIsPropsective = false;
        result = existingSession;
      }
    }
    return result;
  }
  
  private static synchronized HttpSession createNewSession(){
    String id = getNextSessionId();
    FakeSession result = new FakeSession(id);
    fSessions.put(id, result);
    return result;
  }

  private ServletContext fServletContext;
  private long fCreationTime;
  private String fSessionId;
  private boolean fIsPropsective = true;
  private long fLastAccessTime;
  private long fCurrentAccessTime;
  private int fMaxInactiveInterval = 60*30; // 30 minutes by default
  private Map<String, Object> fAttributes = new LinkedHashMap<String, Object>();
  private boolean fHasBeenInvalidated = false;
  private static final Logger fLogger = Util.getLogger(FakeSession.class);
  
  private FakeSession(String aSessionId){
    fCreationTime = System.currentTimeMillis();
    fLastAccessTime = fCreationTime;
    fSessionId = aSessionId;
    try {
      fServletContext = new FakeServletContext();
    }
    catch(FileNotFoundException ex){
      throw new IllegalArgumentException("Cannot construct FakeServletContext.");
    }
  }
  
  private void vomitIfInvalidated() {
    if (fHasBeenInvalidated) throw new IllegalStateException("Session has been invalidated.");
  }

  /** Called only when attempting to join an existing session.  */
  private void updateTimes(){
    //a kind of moving forward of a time interval, with a start and end
    fLastAccessTime = fCurrentAccessTime; //that is, the 'old' value from a previous request
    fCurrentAccessTime = System.currentTimeMillis();
  }
  
  private boolean hasTimedOut(){
    return (fCurrentAccessTime - fLastAccessTime) > fMaxInactiveInterval * 1000;
  }
}
