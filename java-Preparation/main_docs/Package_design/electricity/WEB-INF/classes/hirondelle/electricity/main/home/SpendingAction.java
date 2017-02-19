package hirondelle.electricity.main.home;

import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

public final class SpendingAction extends ActionTemplateListAndEdit {
  
  public SpendingAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser); 
  }
  
  public static final SqlId LIST_SPENDING = new SqlId("LIST_SPENDING");
  public static final SqlId FETCH_SPENDING = new SqlId("FETCH_SPENDING");
  public static final SqlId ADD_SPENDING = new SqlId("ADD_SPENDING");
  public static final SqlId CHANGE_SPENDING = new SqlId("CHANGE_SPENDING");
  public static final SqlId DELETE_SPENDING = new SqlId("DELETE_SPENDING");
  
  public static final RequestParameter ID = RequestParameter.withLengthCheck("Id");
  public static final RequestParameter DATE_PAID = RequestParameter.withLengthCheck("DatePaid");
  public static final RequestParameter PAYMENT = RequestParameter.withLengthCheck("Payment");
  public static final RequestParameter KWH = RequestParameter.withLengthCheck("KilowattHours");
  public static final RequestParameter IS_ESTIMATED = RequestParameter.withRegexCheck("IsEstimated", "(true|false)");
  public static final RequestParameter FACILITY = RequestParameter.withRegexCheck("Facility", "(\\d)+"); 
  public static final RequestParameter COMMENT = RequestParameter.withLengthCheck("Comment");
  
  @Override protected void doList() throws DAOException {
    fLogger.fine("Listing all Spending items.");
    addToRequest(ITEMS_FOR_LISTING, fDAO.list());
  }
  
  @Override protected void validateUserInput() {
    fLogger.fine("Validating user input.");
    ModelFromRequest builder = new ModelFromRequest(getRequestParser());
    try {
      fModel = builder.build(Spending.class, ID, DATE_PAID, PAYMENT, KWH, IS_ESTIMATED, FACILITY, COMMENT);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }
  }
  
  @Override protected void attemptAdd() throws DAOException {
    fLogger.fine("Attempting to add new Spending item.");
    fDAO.add(fModel);
    addMessage("Item added successfully.");
  }
  
  @Override protected void attemptFetchForChange() throws DAOException {
    fLogger.fine("Attempting to fetch an existing Spending item.");
    Spending model = fDAO.fetch(getIdParam(ID));
    if( model == null ){
      addError("Item no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, model);
    }    
  }
  
  @Override protected void attemptChange() throws DAOException {
    fLogger.fine("Attempting to change an existing Spending item.");
    boolean success = fDAO.change(fModel);
    if (success){
      addMessage("Item changed successfully.");
    }
    else {
      addError("No update occurred. Item likely deleted by another user.");
    }
  }
  
  @Override  protected void attemptDelete() throws DAOException {
    fLogger.fine("Attempting to delete an existing Spending item.");
    fDAO.delete(getIdParam(ID));
    addMessage("Item deleted successfully.");    
  }
  
  // PRIVATE 
  private static final ResponsePage FORWARD = new ResponsePage("Spending", "view.jsp", SpendingAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("SpendingAction.list");
  private SpendingDAO fDAO = new SpendingDAO();
  private Spending fModel;
  private static final Logger fLogger = Util.getLogger(SpendingAction.class);
}
