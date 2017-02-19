package hirondelle.fish.exercise.encoding;

import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 Display various items related to character encoding.
 
 <P>Meant as a development and debugging tool only. 
*/
public final class EncodingAction extends ActionImpl {

  public EncodingAction(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  public static final RequestParameter TEST = RequestParameter.withLengthCheck("Test");
  
  /** Show a page listing various items related to encoding.  */
  @Override public ResponsePage execute() throws AppException {
    addToRequest("requestHeaders", getRequestHeaders(getRequestParser().getRequest()));
    return getResponsePage();
  }
  
  // PRIVATE
  
  private static final ResponsePage FORWARD = new ResponsePage("Encoding", "view.jsp", EncodingAction.class);
  
  private Map<String, String> getRequestHeaders(HttpServletRequest aRequest){
    Map<String, String> result = new HashMap<String, String>();
    Enumeration items = aRequest.getHeaderNames();
    while( items.hasMoreElements() ){
      String name = (String)items.nextElement();
      String value = aRequest.getHeader(name);
      result.put(name, value);
    }
    return result;
  }
}
