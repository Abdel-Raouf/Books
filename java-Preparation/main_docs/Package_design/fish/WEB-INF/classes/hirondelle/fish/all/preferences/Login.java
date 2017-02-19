package hirondelle.fish.all.preferences;

import hirondelle.web4j.Controller;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.security.LoginTasks;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
Ensures the user preferences are present in session scope.

<P>Upon login, it is often desirable to place user preferences in the session.
However, in applications which use the servlet <tt>security-constraint</tt> mechanism, user 
login is controlled mostly by the container. So, the issue arises of how to place user 
preferences into a session after successful login.

<P>There does not seem to be any suitable items in the existing Servlet API for this task. The 
{@link javax.servlet.http.HttpSessionListener} allows inspection of sessions that have just
 been created, but does not allow inspection of <em>who</em> has logged in.
*/
public final class Login implements LoginTasks {
  
  /** Returns <tt>true</tt> only if an attribute named {@link Controller#LOCALE} is in session scope. */
  public boolean hasAlreadyReacted(HttpSession aSession) {
    return aSession.getAttribute(Controller.LOCALE) != null;
  }
  
  /**
  Ensure the user preferences are in session scope.
  
  <P>In this implementation, there is only one user preference - the language (English or French).
  This method adds a {@link java.util.Locale} object to session scope under a key defined by  
  {@link hirondelle.web4j.Controller#LOCALE}.
  
  <P>If there are many user preferences, and not just one, then it might make sense to place a single  
  object into session scope, which gathers together all such preferences into a single object.
  Note, however, that the <em>default</em> implementations of 
  {@link hirondelle.web4j.request.LocaleSource} and {@link hirondelle.web4j.request.TimeZoneSource}
  are not consistent with such a style, since they expect their data to be stored under separate 
  keys defined in {@link Controller}. 
 */
  public void reactToUserLogin(HttpSession aSession, HttpServletRequest aRequest) throws AppException {
    fLogger.fine("Adding user preferences to session.");
    String userName = aRequest.getUserPrincipal().getName();
    PreferencesDAO dao  = new PreferencesDAO();
    try {
      Preferences prefs = dao.fetch(SafeText.from(userName));
      fLogger.fine("Adding language/locale preference to session: " + prefs.getLocale());
      aSession.setAttribute(Controller.LOCALE, prefs.getLocale());
    }
    catch (DAOException ex){
      throw new AppException("Cannot fetch language preference from database.", ex);
    }
  }

  // PRIVATE 
  private static final Logger fLogger = Util.getLogger(Login.class);
}
