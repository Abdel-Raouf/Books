package hirondelle.fish.translate.translation;

import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.ui.translate.Translation;

/**
 Show a listing of all {@link Translation}s.
 
 <P>Operation performed : {@link TranslationDAO#list()}.
 
 @view TranslationsList.jsp
*/
public final class TranslationsList extends ActionImpl {

  /** Constructor.  */
  public TranslationsList(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** Show a listing of all {@link Translation}s. */
  public ResponsePage execute() throws AppException {
    TranslationDAO dao = new TranslationDAO();
    addToRequest(ITEMS_FOR_LISTING, dao.list());
    return getResponsePage();
  }
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage(
    "List Translations", "TranslationsList.jsp", TranslationsList.class
  );
}
