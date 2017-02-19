package hirondelle.fish.main.reports;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DynamicSql;
import hirondelle.web4j.database.Report;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;

import java.util.List;
import java.util.Map;

/**
 Number of visits to each restaurant.
 
 @sql reports.sql VISITS_PER_RESTO
 @view Report1.jsp
*/
public final class Report1Action extends ActionImpl {

  /** Constructor.  */
  public Report1Action(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** Fetch report data and display it. */
  public ResponsePage execute() throws AppException {
    addToRequest(ITEMS_FOR_LISTING, getListing());
    return getResponsePage();
  }
  
  public static final SqlId REPORT_SQL = new SqlId("VISITS_PER_RESTO");
  /** Used for sorting mechanism.  */
  public static final RequestParameter SORT_ON = ReqParam.SORT_ON;
  /** Used for sorting mechanism.  */
  public static final RequestParameter ORDER = ReqParam.ORDER;
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage("Report 1", "Report1.jsp", Report1Action.class);
  private static final String DEFAULT_SORT = "ORDER BY 2 DESC, 1 ASC";
  
  /** 
   Returns a List of Maps.
   
   <P>The Map key is column name, and the map value is the column value. 
  */
  private List<Map<String, SafeText>> getListing() throws DAOException {
    return Report.raw(REPORT_SQL, getCustomSort());
  }
  
  private DynamicSql getCustomSort(){
    return getOrderBy(SORT_ON, ORDER, DEFAULT_SORT);
  }
}
