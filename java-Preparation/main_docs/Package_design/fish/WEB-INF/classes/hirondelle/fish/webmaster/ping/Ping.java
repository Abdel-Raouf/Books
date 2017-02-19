package hirondelle.fish.webmaster.ping;

import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.request.RequestParser;

/**
 Display a page listing several links to sanity pages. 
 
 @view view.jsp
*/

public final class Ping extends ActionImpl {
  
  /** Constructor. */
  public Ping(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** Show the page. */
  public ResponsePage execute(){
    return getResponsePage();
  }
  
  private static final ResponsePage FORWARD = new ResponsePage("Ping", "view.jsp", Ping.class); 
}
