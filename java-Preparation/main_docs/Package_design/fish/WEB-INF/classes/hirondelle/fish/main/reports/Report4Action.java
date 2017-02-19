package hirondelle.fish.main.reports;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
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
 Total attendance per year.
 
 <P>Each time a person attends a lunch counts as "1".
 
 @sql reports.sql PARTICIPATION_PER_YEAR, TOTAL_ATTENDANCE
 @view Report4.jsp
*/
public final class Report4Action extends ActionImpl {

  /** Constructor.  */
  public Report4Action(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** Fetch report data and display it.  */
  public ResponsePage execute() throws AppException {
    addToRequest(ITEMS_FOR_LISTING, getListing());
    addToRequest(TOTAL_ATTENDANCE_ALL_YEARS, getTotal());
    return getResponsePage();
  }
  
  public static final SqlId LISTING = new SqlId("PARTICIPATION_PER_YEAR");
  public static final SqlId TOTAL_ATTENDANCE = new SqlId("TOTAL_ATTENDANCE");
  /** Used for sorting mechanism.  */
  public static final RequestParameter SORT_ON = ReqParam.SORT_ON;
  /** Used for sorting mechanism.  */
  public static final RequestParameter ORDER = ReqParam.ORDER;
  
  /** Key for total placed in request scope.  */
  static final String TOTAL_ATTENDANCE_ALL_YEARS = "totalAttendanceAllYears";
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage("Report 4", "Report4.jsp", Report4Action.class);
  private static final String DEFAULT_ORDER_BY = "ORDER BY 1 ASC";
  
  /** 
   Returns a List of Maps.
   
   The Map key is column name, and the map value is the column value. 
  */
  private List<Map<String, SafeText>> getListing() throws DAOException {
    return Report.raw(LISTING, getCustomSort());
  }
  
  private Integer getTotal() throws DAOException {
    return Db.fetchValue(Integer.class, TOTAL_ATTENDANCE);
  }
  
  private DynamicSql getCustomSort(){
    return getOrderBy(SORT_ON, ORDER, DEFAULT_ORDER_BY);
  }
}

