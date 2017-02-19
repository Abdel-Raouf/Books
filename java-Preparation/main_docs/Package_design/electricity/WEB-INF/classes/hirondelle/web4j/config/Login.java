package hirondelle.web4j.config;

import hirondelle.web4j.model.AppException;
import hirondelle.web4j.security.LoginTasks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
Implementation of {@link LoginTasks}, required by WEB4J.

<P>For this simple tutorial, this implementation does nothing.
 Many applications would use this class to place items in session scope, 
 in reaction to a successful user login. 
*/
public final class Login implements LoginTasks {

  /** Returns <tt>false</tt>. */
  public boolean hasAlreadyReacted(HttpSession aSession) {
    return false;
  }
  
  /** Does nothing. */
  public void reactToUserLogin(HttpSession aExistingSession, HttpServletRequest aRequest) throws AppException{
  }
  
  
}
