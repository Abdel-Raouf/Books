package hirondelle.fish.main.visit;

import java.util.Locale;
import java.util.logging.Logger;
import hirondelle.web4j.Controller;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.util.Util;

/**
 Example implementation of translation by using multiple JSPs.
 
 <P>The one-JSP-per-language style is implemented by providing an override for 
 {@link Controller#swapResponsePage(ResponsePage , Locale)}, as done here.
 
 <P><span class="highlight">This style of implementing translation is not recommended, 
 since it causes so much repetition of identical markup across many JSPs.</span>
 The services of the <tt>hirondelle.web4j.ui.translate</tt> package should
 be used instead.
  
 <P>Here, only one particular feature is affected - the Visits page, which is 
 split into <tt>Visit_en.jsp</tt> and <tt>Visit_fr.jsp</tt>, when this 
 custom Controller is used.
 
 <P>(The <tt>Visit.jsp</tt> page uses the recommended style of translation, 
 where markup is not repeated across multiple pages.)   
  
 <P>This example is not complete - it merely exercises the general mechanism. To run it, 
 edit the <tt>servlet-class</tt> setting in <tt>web.xml</tt> to refer to <em>this</em> 
 custom Controller class. After starting the app, then navigate to the <tt>Visits</tt> page, 
 and flip the Locale using the <tt>Preferences</tt> page. This will exercise the mechanism.
*/
public final class CustomController extends Controller {
  
  /**
   Returns a non-<tt>null</tt> value only if <tt>aResponsePage</tt> ends with <tt>'Visit.jsp'</tt>. 
  */
  @Override protected ResponsePage swapResponsePage(ResponsePage aResponsePage, Locale aLocale){
    fLogger.fine("Custom Controller : " + aResponsePage);
    ResponsePage result = null;
    if (aResponsePage.toString().endsWith("Visit.jsp")){
      int idx = aResponsePage.toString().indexOf("Visit.jsp");
      String alteredURI = aResponsePage.toString().substring(0,idx) + "Visit_" + aLocale.toString() + ".jsp"; 
      fLogger.fine("Forward Response Page updated for locale: " + alteredURI);
      result = new ResponsePage(alteredURI).setIsRedirect(Boolean.FALSE);
    }
    return result;
  }
  
  // PRIVATE //
  private static final Logger fLogger = Util.getLogger(CustomController.class);
}
