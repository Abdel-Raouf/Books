package hirondelle.fish.translate.refresh;

import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.TranslatorImpl;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParser;

/**
 Refresh the translations held in memory.
 
 <P>Translations are held in memory as a performance optimization. 
 Translations are both commonly needed, and rarely change, so holding them in 
 memory is a reasonable design.
 
 <P>Operation performed : {@link TranslatorImpl#read()}.
 
 @view RefreshTranslations.jsp
*/
public final class RefreshTranslations extends ActionTemplateShowAndApply {

  /** Constructor. */
  public RefreshTranslations(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }

  /** Show the total number of translations, and a form with a single button  */
  protected void show() throws AppException {
    addToRequest(NUM_TRANSLATION_RECORDS, TranslatorImpl.getNumTranslations());
  }
  
  /** Form has no input, just single button - no validation needed. */
  protected void validateUserInput() throws AppException {
    //empty
  }
  
  /** Refresh the in-memory cache of translations.  */
  protected void apply() throws AppException {
    TranslatorImpl.read();
  }
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage(
    "Refresh Translations", "RefreshTranslations.jsp", RefreshTranslations.class
  );
  private static final ResponsePage REDIRECT = new ResponsePage(
    "RefreshTranslations.do?Operation=Show"
  );
  
  private static final String NUM_TRANSLATION_RECORDS = "numTranslations";
}
 
