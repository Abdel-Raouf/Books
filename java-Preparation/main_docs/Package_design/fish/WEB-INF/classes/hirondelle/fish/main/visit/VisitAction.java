package hirondelle.fish.main.visit;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DynamicSql;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

/**
 Edit the {@link Visit}s to a particular {@link hirondelle.fish.main.resto.Resto} on a 
 particular day. 
 
 @sql statements.sql
 @view Visit.jsp
*/
public final class VisitAction extends ActionTemplateListAndEdit {
  
  /** Constructor. */
  public VisitAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }

  public static final SqlId VISIT_LIST =  new SqlId("VISIT_LIST");
  public static final SqlId VISIT_FETCH =  new SqlId("VISIT_FETCH");
  public static final SqlId VISIT_ADD =  new SqlId("VISIT_ADD");
  public static final SqlId VISIT_CHANGE =  new SqlId("VISIT_CHANGE");
  public static final SqlId VISIT_DELETE =  new SqlId("VISIT_DELETE");
  public static final SqlId VISIT_FETCH_NEXT_FUTURE = new SqlId("VISIT_FETCH_NEXT_FUTURE");
  
  public static final RequestParameter VISIT_ID = ReqParam.ID;
  public static final RequestParameter MESSAGE = RequestParameter.withLengthCheck("Message");
  public static final RequestParameter LUNCH_DATE = RequestParameter.withLengthCheck("Lunch Date");
  public static final RequestParameter RESTAURANT = RequestParameter.withLengthCheck("Restaurant");

  public static final RequestParameter SORT_ON = RequestParameter.withRegexCheck("SortOn", "(Resto\\.Name|LunchDate|Message)");
  public static final RequestParameter ORDER = ReqParam.ORDER;
  
  /** List all {@link Visit}s. */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fVisitDAO.list(withCustomSortOrder()));
    fLogger.fine("Adding listing of Visits to request.");
  }
  
  /** Ensure user input can build a {@link Visit}.  */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fVisit = builder.build(Visit.class, VISIT_ID, RESTAURANT, LUNCH_DATE, MESSAGE);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Add a new {@link Visit}. */
  protected void attemptAdd() throws DAOException {
    fVisitDAO.add(fVisit);
    fLogger.fine("Visit added OK.");
    addMessage("Visit to _1_ added successfully.", fVisit.getRestaurant());
  }
  
  /** Fetch an existing {@link Visit} in preparation for editing it. */
  protected void attemptFetchForChange() throws DAOException {
    Visit visit = fVisitDAO.fetch(getIdParam(VISIT_ID));
    if( visit == null ){
      addError("Visit no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, visit);
      fLogger.fine("Adding Visit to request: " + visit);
    }
  }
  
  /** Update an existing {@link Visit}. */
  protected void attemptChange() throws DAOException {
    boolean success = fVisitDAO.change(fVisit);
    if (success){
      addMessage("Visit to _1_ changed successfully.", fVisit.getRestaurant());
    }
    else {
      addError("No update occurred. Visit likely deleted by another user.");
    }
  }
  
  /** Delete an existing {@link Visit}. */
  protected void attemptDelete() throws DAOException {
    VisitDAO dao = new VisitDAO();
    try {
      dao.delete(getIdParam(VISIT_ID));
      addMessage("Visit deleted successfully.");
    }
    catch(DAOException ex){
      addError("Cannot delete. Visit used elsewhere.");      
    }
  }
  
  // PRIVATE //
  private Visit fVisit;
  private VisitDAO fVisitDAO = new VisitDAO();
  private static final Logger fLogger = Util.getLogger(VisitAction.class);
  private static final ResponsePage FORWARD = new ResponsePage(
    "Visits", "Visit.jsp", VisitAction.class
  );
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("VisitAction.list");
  private static final String DEFAULT_ORDER = "ORDER BY LunchDate DESC";
  
  private DynamicSql withCustomSortOrder(){
    return getOrderBy(SORT_ON, ORDER, DEFAULT_ORDER);
  }
}
