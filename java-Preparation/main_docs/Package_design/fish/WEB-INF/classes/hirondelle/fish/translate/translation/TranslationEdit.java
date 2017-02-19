package hirondelle.fish.translate.translation;

import hirondelle.fish.main.codes.CodeTable;
import hirondelle.fish.translate.basetext.BaseText;
import hirondelle.fish.translate.basetext.BaseTextDAO;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.Code;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.ui.translate.Translation;
import hirondelle.web4j.util.Util;

import java.util.Locale;
import java.util.logging.Logger;

/**
 Edit all {@link Translation}s.
 
 @sql statements.sql LIST, ADD, FETCH, CHANGE, DELETE  
 @view TranslationEdit.jsp
*/
public final class TranslationEdit extends ActionTemplateListAndEdit {

  /** Constructor. */
  public TranslationEdit(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }
    
  public static final SqlId LIST =  getSqlId("LIST");
  public static final SqlId FETCH =  getSqlId("FETCH");
  public static final SqlId ADD =  getSqlId("ADD");
  public static final SqlId CHANGE = getSqlId("CHANGE");
  public static final SqlId DELETE =  getSqlId("DELETE");
    
  public static final RequestParameter LOCALE_ID = RequestParameter.withLengthCheck("LocaleId");
  public static final RequestParameter TRANSLATION = RequestParameter.withLengthCheck("Translation");
  public static final RequestParameter BASE_TEXT_ID = RequestParameter.withLengthCheck("BaseTextId");
  
  /** Display a listing of all {@link Translation}s.  */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fTranslationDAO.listFor(getParent().getId()));
    addToRequest(PARENT, getParent());
  }
  
  /** Ensure user input can create a {@link Translation}. */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fTranslation = builder.build(Translation.class, getParent().getBaseText(), fetchLocale(), TRANSLATION, getParent().getId(), LOCALE_ID);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Add a new {@link Translation}. */
  protected void attemptAdd() throws DAOException {
    fLogger.fine("Base Locale " + BASE_LOCALE);
    fLogger.fine("Translation " + fTranslation);
    fLogger.fine("Is Coder Key " + getParent().getIsCoderKey());
    try {
      if( BASE_LOCALE.equals(fTranslation.getLocale()) &&  ! getParent().getIsCoderKey().booleanValue()){
        addError("Invalid selection : can add a translation for base language only when Base Text is a Coder Key.");
      }
      else {
        fTranslationDAO.add(fTranslation);
        addParentIdToRedirectPage();
        addMessage("Item added successfully.");
      }
    }
    catch (DuplicateException ex){
      addError("Only one translation allowed per Locale."); 
    }
    catch(DAOException ex){
      addError("Unable to add Translation.");
    }
  }
  
  /** Fetch a {@link Translation} in preparation for editing it. */
  protected void attemptFetchForChange() throws DAOException {
    //Locale or Base or Translation might be gone
    Translation translation = fTranslationDAO.fetch(getParent().getId(), getIdParam(LOCALE_ID));
    if( translation == null ){
      addError("Translation no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, translation);
    }
  }
  
  /** Update an existing {@link Translation}. */
  protected void attemptChange() throws DAOException {
    //change the text only, not the locale
    try {
      fTranslationDAO.change(fTranslation);
      addParentIdToRedirectPage();
      addMessage("Item changed successfully.");
    }
    catch (DAOException ex){
      addError("No update occurred."); 
    }
  }
  
  /** Delete an existing {@link Translation}. */
  protected void attemptDelete() throws DAOException {
    try { 
      fTranslationDAO.delete(getParent().getId(), getIdParam(LOCALE_ID));
      addParentIdToRedirectPage();
      addMessage("Item deleted successfully.");
    }
    catch(DAOException ex){
      addError("No delete occurred.");
    }
  }
  
  // PRIVATE //
  private BaseText fParent;
  private Translation fTranslation;
  private TranslationDAO fTranslationDAO = new TranslationDAO();
  private static final Logger fLogger = Util.getLogger(TranslationEdit.class);
  private static final ResponsePage FORWARD = new ResponsePage("Edit Translations", "TranslationEdit.jsp", TranslationEdit.class);
  /** Always need to dynamically add the BaseTextId to this ResponsePage. */
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("TranslationEdit.do?Operation=List");
  private static final String PARENT = "parentItem";
  private static final String BASE_LOCALE = "en";

  /**
   Every operation requires that the parent object be retrieved once at the start of the action, 
   before other processing takes place, and placed in a field.
   
   <P>As well, some operations require the parent to be placed in request scope.
  */
  private BaseText getParent() {
    if( fParent == null){
      BaseTextDAO dao = new BaseTextDAO();
      //this req param needs to always be present
      Id baseTextId = getIdParam(BASE_TEXT_ID);
      fLogger.fine("Fetching BaseText for " + Util.quote(baseTextId));
      try { 
        fParent = dao.fetch(baseTextId);
      }
      catch(DAOException ex){
        fLogger.severe("Cannot fetch BaseText using id " + Util.quote(baseTextId));        
      }
    }
    return fParent;
  }

  /** 
   Add the BaseTextId to the ResponsePage URI.
   
   <P>Called for every successful edit operation, so the listing will not 'forget' the parent.
  */
  private void addParentIdToRedirectPage(){
    addDynamicParameterToRedirectPage(BASE_TEXT_ID.getName(), getParent().getId().toString());
  }
  
  /** Translate Locale id into text. If not present, return <tt>null</tt>. */
  private Locale fetchLocale(){
    Locale result = null;
    String localeId = getParamUnsafe(LOCALE_ID);
    if ( Util.textHasContent(localeId) ){
      Code code = CodeTable.codeFor( Id.from(localeId), CodeTable.SUPPORTED_LOCALES);
      result = Util.buildLocale(code.getText().getRawString());
    }
    return result;
  }
  
  private static SqlId getSqlId(String aSqlStatementId){
    return new SqlId(ConnectionSrc.TRANSLATION, aSqlStatementId);
  }
}

