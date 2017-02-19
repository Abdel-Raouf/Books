package hirondelle.fish.translate.unknown;

import hirondelle.fish.translate.basetext.BaseText;
import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.config.TranslatorImpl;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.ui.translate.Translator;

/**
 Decide what to do with unknown {@link BaseText}.
 
 <P>See {@link Translator} for a definition of <tt>BaseText</tt>.
 
 @sql statements.sql
 @view view.jsp
*/
public final class UnknownBaseTextEdit extends ActionTemplateListAndEdit {
  
  /** Constructor.  */
  public UnknownBaseTextEdit(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }
    
  public static final SqlId LIST = getSqlId("UNKNOWN_LIST");
  public static final SqlId DELETE = getSqlId("UNKNOWN_DELETE");
  public static final SqlId DELETE_ALL = getSqlId("UNKNOWN_DELETE_ALL");
  public static final SqlId ADD = getSqlId("UNKNOWN_ADD");
  public static final SqlId COUNT = getSqlId("UNKNOWN_COUNT");
  
  public static final RequestParameter ID = ReqParam.ID;
  public static final RequestParameter BASE_TEXT = RequestParameter.withLengthCheck("BaseText");
  public static final RequestParameter IS_CODER_KEY = RequestParameter.withLengthCheck("IsCoderKey");

  static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("UnknownBaseTextEdit.do?Operation=List");
  
  /** Value {@value} - key for item placed in request scope. Identifies if currently recording.  */
  public static final String IS_RECORDING = "isRecording";

  /** List all unknown base text items. */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fUnknownBaseTextDAO.list());
    addToRequest(IS_RECORDING, new Boolean(TranslatorImpl.isRecording()) );
  }
  
  /** Ensure user input can build a {@link BaseText}. */
  protected void validateUserInput() {
    //here, there is no explicit user input; hidden form params are used 
    //to pass in data instead.
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fBaseText = builder.build(BaseText.class, ID, BASE_TEXT, IS_CODER_KEY);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }

  /**
   Add to the <tt>BaseText</tt> table, as either a coder key, or as a 
   natural language key.
  */
  protected void attemptAdd() throws DAOException {
    try {
      fUnknownBaseTextDAO.move(fBaseText);
      addMessage("Item accepted as BaseText successfully.");
    }
    catch (DuplicateException ex){
      addError("Base Text already taken. Must be unique.");  
    }
  }
  
  /** Unsupported operation. */
  protected void attemptFetchForChange() {
    throw new UnsupportedOperationException();
  }
  
  /** Unsupported operation. */
  protected void attemptChange() {
    throw new UnsupportedOperationException();
  }
  
  /** Delete an unknown base text item. */
  protected void attemptDelete() throws DAOException {
    //Here, the id is taken as the unknown base text. 
    //Special case : the IgnorableParamValue for SELECT controls will often appear 
    //as an unknown. This is why the "old-fashioned" way of retrieving the value is used.
    String baseText = getRequestParser().getRequest().getParameter("BaseText");
    int numRecords = fUnknownBaseTextDAO.delete(baseText);
    addMessage("_1_ record(s) deleted.", new Integer(numRecords));
  }
  
  // PRIVATE //
  private BaseText fBaseText;
  private UnknownBaseTextDAO fUnknownBaseTextDAO = new UnknownBaseTextDAO();
  private static final ResponsePage FORWARD = new ResponsePage("Unknowns", "view.jsp", UnknownBaseTextEdit.class);
  
  private static SqlId getSqlId(String aSqlStatementId){
    return new SqlId(ConnectionSrc.TRANSLATION, aSqlStatementId);
  }
}
