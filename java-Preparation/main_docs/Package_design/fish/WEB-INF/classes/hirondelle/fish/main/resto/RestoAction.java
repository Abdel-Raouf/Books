package hirondelle.fish.main.resto;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.Startup;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

/**
 Edit {@link Resto} objects.
 
 <P><tt>Resto</tt> objects form a code table. When a successful edit occurs, then this 
 class will refresh the code table held in memory.
 
 @sql statements.sql
 @view view.jsp
*/
public final class RestoAction extends ActionTemplateListAndEdit {

  /** Constructor.  */
  public RestoAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }
  
  static public final SqlId LIST_RESTOS =  new SqlId("LIST_RESTOS");
  static public final SqlId FETCH_RESTO =  new SqlId("FETCH_RESTO");
  static public final SqlId ADD_RESTO =  new SqlId("ADD_RESTO");
  static public final SqlId CHANGE_RESTO =  new SqlId("CHANGE_RESTO");
  static public final SqlId DELETE_RESTO =  new SqlId("DELETE_RESTO");
  
  public static final RequestParameter RESTO_ID = ReqParam.ID;
  public static final RequestParameter NAME = ReqParam.NAME;
  public static final RequestParameter LOCATION = RequestParameter.withLengthCheck("Location");
  public static final RequestParameter PRICE = RequestParameter.withLengthCheck("Price");
  public static final RequestParameter COMMENT = RequestParameter.withLengthCheck("Comment");
  
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fRestoDAO.list());
  }
  
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fResto = builder.build(Resto.class, RESTO_ID, NAME, LOCATION, PRICE, COMMENT);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  protected void attemptAdd() throws DAOException {
    try {
      fRestoDAO.add(fResto);
      addMessage("_1_ added successfully.", fResto.getName());
      refreshPickLists();
    }
    catch(DuplicateException ex){
      addError("Restaurant Name already taken. Please use another name.");  
    }
  }
  
  protected void attemptFetchForChange() throws DAOException {
    Resto resto = fRestoDAO.fetch(getIdParam(RESTO_ID));
    if( resto == null ){
      addError("Item no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, resto);
    }
  }
  
  protected void attemptChange() throws DAOException {
    try {
      boolean success = fRestoDAO.change(fResto);
      if (success){
        addMessage("_1_ changed successfully.", fResto.getName());
        refreshPickLists();
      }
      else {
        addError("No update occurred. Item likely deleted by another user.");
      }
    }
    catch(DuplicateException ex){
      addError("Restaurant Name already taken. Please use another name.");  
    }
  }
  
  protected void attemptDelete() throws DAOException {
    try {
      fRestoDAO.delete(getIdParam(RESTO_ID));
      addMessage("Item deleted successfully.");
      refreshPickLists();
    }
    catch (DAOException ex){
      addError("Cannot delete. Item likely used elsewhere.");      
    }
  }
  
  // PRIVATE //
  private Resto fResto;
  private RestoDAO fRestoDAO = new RestoDAO();
  private static final ResponsePage FORWARD = new ResponsePage("Restaurants", "view.jsp", RestoAction.class);
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("RestoAction.list");
  private static final Logger fLogger = Util.getLogger(RestoAction.class);
  
  private void refreshPickLists() throws DAOException {
    fLogger.fine("Updating all Pick Lists, including Restos, in application scope.");
    Startup.lookUpCodeTablesAndPlaceIntoAppScope();
  }
}
