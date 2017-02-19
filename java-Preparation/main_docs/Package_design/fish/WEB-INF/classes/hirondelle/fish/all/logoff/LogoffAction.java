package hirondelle.fish.all.logoff;

import hirondelle.web4j.model.AppException;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.action.ResponsePage;
import javax.servlet.http.Cookie;

/** Log off the application. */
public final class LogoffAction extends ActionImpl {
  
  /** Constructor. */
  public LogoffAction(RequestParser aRequestParser){
    super(REDIRECT, aRequestParser);
  }
  
  /**
   Perform all operations related to ending a session.
     
   <P>Expire the session cookie, end the session, and redirect to the log off page.
   A redirect is used to avoid problems with browser reloads.
   The response includes a header which instructs the browser to explicitly delete its 
   <tt>JSESSIONID</tt>cookie.
  */
  public ResponsePage execute() throws AppException {
    Cookie[] cookies = getRequestParser().getRequest().getCookies();
    for(Cookie cookie : cookies){
      if( cookie.getName().equalsIgnoreCase(SESSION_COOKIE_NAME)) {
        cookie.setMaxAge(DELETE_IMMEDIATELY);
        cookie.setValue(TEMP_COOKIE_VALUE);
        cookie.setPath(getContext()); 
        getRequestParser().getResponse().addCookie(cookie);
      }
    }
    endSession();
    return getResponsePage();
  }
  
  // PRIVATE //
  private static final ResponsePage REDIRECT = new ResponsePage("../../Logoff.jsp");
  private static final String SESSION_COOKIE_NAME = "JSESSIONID";
  /** This value is never really used, since the cookie is immediately deleted by the browser. */
  private static final String TEMP_COOKIE_VALUE = "EXPIRED";
  /** This expiration time instructs the browser to delete the cookie immediately. */
  private static final int DELETE_IMMEDIATELY = 0;
  
  private String getContext(){
    return getRequestParser().getRequest().getContextPath();
  }
}
