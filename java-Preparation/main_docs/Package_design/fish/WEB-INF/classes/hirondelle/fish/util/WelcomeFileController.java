package hirondelle.fish.util;

import hirondelle.web4j.util.Util;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 Redirect requests for the home page to a specific Action.
 
 <P>Configuration in web.xml :<br>
 This Controller servlet is mapped to '/welcome', and a corresponding welcome-file entry 
 is listed as 'welcome', without the leading slash.
*/
public final class WelcomeFileController  extends HttpServlet {
  
  @Override public void init(ServletConfig aConfig) throws ServletException {
    super.init(aConfig);
    fLogger.config("WelcomeFile Controller - starting.");
    fDestination = aConfig.getInitParameter("Destination");
    if ( ! Util.textHasContent(fDestination) ) {
      fLogger.severe("Destination URL needed, but not configured in web.xml.");
    }
  }
  
  @Override public void destroy(){
    fLogger.config("WelcomeFile Controller - ending.");
  }
  
  @Override protected void doGet(
    HttpServletRequest aRequest, HttpServletResponse aResponse
  ) throws ServletException, IOException {
    fLogger.fine("Redirecting directory request to new location : " + fDestination);
    aResponse.setContentType("text/html");
    aResponse.sendRedirect(fDestination);
  }
  
  // PRIVATE //
  private String fDestination;
  private static final Logger fLogger = Util.getLogger(WelcomeFileController.class);
}
