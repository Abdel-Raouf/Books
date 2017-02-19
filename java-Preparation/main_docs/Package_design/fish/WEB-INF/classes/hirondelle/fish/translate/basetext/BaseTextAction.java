package hirondelle.fish.translate.basetext;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;

/**
 Edit {@link BaseText} items.
 
 <P>This action called when URI matches the value {@link #EXPLICIT_URI_MAPPING}. 
 (This is an exception to the usual, implicit mapping style. It exists merely to demonstrate 
 the technique.)
 
 @sql statements.sql
 @view view.jsp
*/
public final class BaseTextAction extends ActionTemplateListAndEdit {

  /** Constructor. */
  public BaseTextAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }

  public static final SqlId BASE_TEXT_LIST = getTranslationSqlId("BASE_TEXT_LIST");
  public static final SqlId BASE_TEXT_FETCH = getTranslationSqlId("BASE_TEXT_FETCH");
  public static final SqlId BASE_TEXT_ADD = getTranslationSqlId("BASE_TEXT_ADD");
  public static final SqlId BASE_TEXT_CHANGE = getTranslationSqlId("BASE_TEXT_CHANGE");
  public static final SqlId BASE_TEXT_DELETE = getTranslationSqlId("BASE_TEXT_DELETE");
    
  /** 
   URI element used to map requests to this action - {@value}.
      
   <P>This is an example of an <em>explicit</em> URI mapping. If this field 
   was absent, then the <em>implicit</em> URI mapping for this 
   {@link hirondelle.web4j.action.Action} would be used.
   
   <P>See {@link hirondelle.web4j.request.RequestParserImpl} for more information
   on implicit and explicit URI mapping.
  */
  public static final String EXPLICIT_URI_MAPPING = "/translate/basetext/BaseTextEdit";
  
  public static final RequestParameter BASE_TEXT_ID = ReqParam.ID;
  public static final RequestParameter BASE_TEXT = RequestParameter.withLengthCheck("Base Text");
  public static final RequestParameter IS_CODER_KEY = RequestParameter.withLengthCheck("Is Coder Key");
  
  /** Show all {@link BaseText} items. */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fBaseTextDAO.list());
  }
  
  /** Ensure user input can build a {@link BaseText}. */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fBaseText = builder.build(BaseText.class, BASE_TEXT_ID, BASE_TEXT, IS_CODER_KEY);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Add a new {@link BaseText}. */
  protected void attemptAdd() throws DAOException {
    try {
      fBaseTextDAO.add(fBaseText);
      addMessage("Item added successfully.");
    }
    catch (DuplicateException ex){
      addError("Base Text already taken. Must be unique.");  
    }
  }
  
  /** Fetch a {@link BaseText} in preparation for editing it. */
  protected void attemptFetchForChange() throws DAOException {
    BaseText baseText = fBaseTextDAO.fetch(getIdParam(BASE_TEXT_ID));
    if( baseText == null ){
      addError("Base Text no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, baseText);
    }
  }
  
  /** Update an existing {@link BaseText}. */
  protected void attemptChange() throws DAOException {
    try {
      boolean success = fBaseTextDAO.change(fBaseText);
      if (success){
        addMessage("Item changed successfully.");
      }
      else {
        addError("No update occurred. Item likely deleted by another user.");
      }
    }
    catch (DuplicateException ex){
      addError("Base Text already taken. Please choose a different Base Text.");
    }
  }
  
  /** Delete an existing {@link BaseText}. */
  protected void attemptDelete() throws DAOException {
    try {
      fBaseTextDAO.delete(getIdParam(BASE_TEXT_ID));
      addMessage("Item deleted successfully.");
    }
    catch(DAOException ex){
      addError("Cannot delete. Item used elsewhere.");      
    }
  }
  
  // PRIVATE //
  private BaseText fBaseText;
  private BaseTextDAO fBaseTextDAO = new BaseTextDAO();
  private static final ResponsePage FORWARD = new ResponsePage("Base Text", "view.jsp", BaseTextAction.class);
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("BaseTextEdit.do?Operation=List");
  
  private static SqlId getTranslationSqlId(String aSqlStatementId){
    return new SqlId(ConnectionSrc.TRANSLATION, aSqlStatementId);
  }
}
