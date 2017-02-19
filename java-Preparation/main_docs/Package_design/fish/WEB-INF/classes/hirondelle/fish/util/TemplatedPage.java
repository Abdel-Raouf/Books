package hirondelle.fish.util;

import hirondelle.web4j.action.ResponsePage;

/**
 Create templated {@link ResponsePage} objects.
 
 <P><span class='highlight'>This implementation assumes that related JSPs and classes reside in 
 the same directory.</span> This policy is unusual, but recommended. Having <em>all</em> items 
 related to a feature in the same directory - classes, JSPs, and .sql files - is highly 
 satisfying and natural. 
*/
public final class TemplatedPage {
  
  /**
   Return a templated {@link ResponsePage}.
   
   <P>This method simply forwards all parameters to 
   {@link ResponsePage#ResponsePage(String, String, String, java.lang.Class)}. Please 
   see that constructor for important information.  
  */
  public static ResponsePage get(String aTitle, String aBodyJsp, Class aRepresentativeClass) {
    return new ResponsePage(aTitle, aBodyJsp, MAIN, aRepresentativeClass);
  }

  // PRIVATE //
  
  //The response page URL is relative to each feature's context. 
  //One name can actually refer to N .JSPs, according to context.
  //Here, there are multiple Template.jsp files, one for each module (that is, for each set of related features.)
  private static final String MAIN = "../Template.jsp";
}
