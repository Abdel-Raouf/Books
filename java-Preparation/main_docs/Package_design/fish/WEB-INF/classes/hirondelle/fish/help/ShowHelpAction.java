package hirondelle.fish.help;

import java.util.logging.Logger;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.action.Action;
import hirondelle.web4j.util.Util;

/**
 Toggle the display of help text, and redirect back to the original URI.
 
  <P>This class interacts with {@link ShowHelpTag} by placing an item in session scope, 
  using a {@link hirondelle.fish.help.ShowHelpTag#KEY} defined by that class.
  
  <P>The original URI is taken from a request parameter named 'OriginalURI', whose value in turn  
  comes from an item named {@link hirondelle.web4j.Controller#CURRENT_URI}, placed in request 
  scope by the Controller.
*/
public final class ShowHelpAction implements Action {
  
  /** Constructor. */
  public ShowHelpAction(RequestParser aRequestParser){
    fRequestParser = aRequestParser;
  }
  
  public static final RequestParameter ORIGINAL_URI = RequestParameter.withLengthCheck("OriginalURI"); 
  
  /** 
   Toggle the display of help text, and redirect back to original request.
   
   <P>Toggles the value of a {@link Boolean} item in session scoped, identified by 
   {@link ShowHelpTag#KEY}.
  */
  public ResponsePage execute() throws AppException {
    toggleDisplayOfHelp();
    return new ResponsePage(getRedirect());
  }
  
  // PRIVATE //
  private final RequestParser fRequestParser;
  private static final Logger fLogger = Util.getLogger(ShowHelpAction.class);
  private static final boolean DO_NOT_CREATE = false;
  
  private void toggleDisplayOfHelp() {
    fLogger.fine("Toggling display of help.");
    Boolean oldSetting = (Boolean)fRequestParser.getRequest().getSession(DO_NOT_CREATE).getAttribute(ShowHelpTag.KEY);
    Boolean newSetting = null; 
    if ( oldSetting == null || oldSetting == Boolean.FALSE ){
      newSetting = Boolean.TRUE;
    }
    else {
      newSetting = Boolean.FALSE;
    }
    fRequestParser.getRequest().getSession(DO_NOT_CREATE).setAttribute(ShowHelpTag.KEY, newSetting);
  }
  
  private String getRedirect() {
    fLogger.fine("Getting original URI.");
    String result = fRequestParser.toSafeText(ORIGINAL_URI).getRawString();
    result = Util.replace(result, "&amp;", "&");
    fLogger.fine("Redirecting to : " + result);
    return result;
  }
}
