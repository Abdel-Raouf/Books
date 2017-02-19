package hirondelle.fish.webmaster.diagnostics;

import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.Controller;

/**
 Remove the {@link hirondelle.web4j.webmaster.TroubleTicket} held in application scope.
 
 <P>When a <tt>TroubleTicket</tt> occurs, it is placed in application scope. 
 There, it may be later retrieved and examined. Once it has been 
 investigated and resolved, it should be removed from application scope, using this <tt>Action</tt>.
 
 <P>Only one <tt>TroubleTicket</tt> at a time is stored in application scope. 
 If a new one occurs, it overwrites any existing <tt>TroubleTicket</tt>, if any.
*/
public final class ClearTroubleTicket extends ActionImpl {
  
  /** Constructor. */
  public ClearTroubleTicket(RequestParser aRequestParser){
    super(REDIRECT, aRequestParser);
  }
  
  /** 
   Remove the {@link hirondelle.web4j.webmaster.TroubleTicket} from application scope, 
   and re-display the diagnostics page using {@link ShowDiagnostics}.
   
   Uses {@link hirondelle.web4j.Controller#MOST_RECENT_TROUBLE_TICKET} as key. 
  */
  public ResponsePage execute() {
    getExistingSession().getServletContext().removeAttribute(Controller.MOST_RECENT_TROUBLE_TICKET); 
    return getResponsePage();
  }
  
  // PRIVATE //
  private static final ResponsePage REDIRECT = new ResponsePage("ShowDiagnostics.do");
}
