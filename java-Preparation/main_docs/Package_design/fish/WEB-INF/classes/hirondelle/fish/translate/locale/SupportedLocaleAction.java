package hirondelle.fish.translate.locale;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.config.Startup;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;

import java.util.Locale;

/**
 Edit the {@link Locale}s supported by this application.
 
 <P>Editing these items is expected to be an uncommon operation. Such edits 
 would usually occur only during development.
 
 @sql statements.sql
 @view view.jsp
*/
public final class SupportedLocaleAction extends ActionTemplateListAndEdit {
  
  /** Constructor. */
  public SupportedLocaleAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }

  public static final SqlId LOCALE_LIST =  getTranslationSqlId("LOCALE_LIST");
  public static final SqlId LOCALE_FETCH =  getTranslationSqlId("LOCALE_FETCH");
  public static final SqlId LOCALE_ADD =  getTranslationSqlId("LOCALE_ADD");
  public static final SqlId LOCALE_CHANGE =  getTranslationSqlId("LOCALE_CHANGE");
  public static final SqlId LOCALE_DELETE =  getTranslationSqlId("LOCALE_DELETE");
  
  public static final RequestParameter SUPPORTED_LOCALE_ID = ReqParam.ID;
  public static final RequestParameter SHORT_FORM = RequestParameter.withLengthCheck("Short Form");
  public static final RequestParameter DESCRIPTION = RequestParameter.withLengthCheck("Description");

  /** List all {@link SupportedLocale}s. */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fSupportedLocaleDAO.list());
  }
  
  /** Ensure user input can build a {@link SupportedLocale}. */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fSupportedLocale = builder.build(SupportedLocale.class, SUPPORTED_LOCALE_ID, SHORT_FORM, DESCRIPTION);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Add a new {@link SupportedLocale}. */
  protected void attemptAdd() throws DAOException {
    try {
      fSupportedLocaleDAO.add(fSupportedLocale);
      addMessage("_1_ added successfully.", fSupportedLocale.getDescription());
      refreshPickLists();
    }
    catch (DuplicateException ex){
      addError("Locale already taken. Please use another Short Form.");  
    }
  }
  
  /** Fetch an existing {@link SupportedLocale}. */
  protected void attemptFetchForChange() throws DAOException {
    SupportedLocale locale = fSupportedLocaleDAO.fetch(getIdParam(SUPPORTED_LOCALE_ID));
    if( locale == null ){
      addError("Locale no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, locale);
    }
  }
  
  /** Update an exisiting {@link SupportedLocale}. */
  protected void attemptChange() throws DAOException {
    try {
      boolean success = fSupportedLocaleDAO.change(fSupportedLocale);
      if (success){
        addMessage("Locale for _1_ changed successfully.", fSupportedLocale.getDescription());
        refreshPickLists();
      }
      else {
        addError("No update occurred. Item likely deleted by another user.");
      }
    }
    catch (DuplicateException ex){
      addError("Short Form already taken. Please choose a different Short Form.");
    }
  }

  /** Delete an existing {@link SupportedLocale}. */
  protected void attemptDelete() throws DAOException {
    try {
      fSupportedLocaleDAO.delete(getIdParam(SUPPORTED_LOCALE_ID));
      addMessage("Item deleted successfully.");
      refreshPickLists();
    }
    catch(DAOException ex){
      addError("Cannot delete. Item used elsewhere.");      
    }
  }
  
  // PRIVATE //
  private SupportedLocale fSupportedLocale;
  private SupportedLocaleDAO fSupportedLocaleDAO = new SupportedLocaleDAO();
  
  private static final ResponsePage FORWARD = new ResponsePage(
    "Locales", "view.jsp", SupportedLocaleAction.class
  );
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("SupportedLocaleAction.do?Operation=List");
  
  private void refreshPickLists() throws DAOException {
    Startup.lookUpCodeTablesAndPlaceIntoAppScope();
  }
  
  private static SqlId getTranslationSqlId(String aSqlStatementId){
    return new SqlId(ConnectionSrc.TRANSLATION, aSqlStatementId);
  }
}
