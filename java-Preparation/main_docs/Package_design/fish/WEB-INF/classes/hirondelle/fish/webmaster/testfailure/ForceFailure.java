package hirondelle.fish.webmaster.testfailure;

import java.util.logging.Logger;

import hirondelle.web4j.model.AppException;
import hirondelle.web4j.action.Action;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.util.Util;

/**
 Deliberately throw a {@link RuntimeException}, to test behavior upon failure.
 
 <P>The webmaster configured in <tt>web.xml</tt> should receive a <tt>TroubleTicket</tt>
 email. 
*/
public final class ForceFailure implements Action {
  
  /** Constructor. */
  public ForceFailure(RequestParser aRequestParser){
    //do nothing
  }

  /** This method deliberately fails. */
  public ResponsePage execute() throws AppException {
    String message = "Testing application behavior upon failure.";
    fLogger.severe(message);
    if ( true ){
      throw new RuntimeException(message);
    }
    return null;
  }
  
  // PRIVATE //
  private static final Logger fLogger = Util.getLogger(ForceFailure.class);
}
