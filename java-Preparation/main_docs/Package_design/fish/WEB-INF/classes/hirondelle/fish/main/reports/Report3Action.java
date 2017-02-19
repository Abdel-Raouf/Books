package hirondelle.fish.main.reports;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DynamicSql;
import hirondelle.web4j.database.Report;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 Number of members that have attended recent lunches.
 
 @sql reports.sql NUM_MEMBERS_FOR_RECENT_VISITS
 @view Report3.jsp
*/
public final class Report3Action extends ActionImpl {

  /** Constructor.  */
  public Report3Action(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** Fetch report data and display it. */  
  public ResponsePage execute() throws AppException {
    addToRequest(ITEMS_FOR_LISTING, getListing());
    return getResponsePage();
  }
  
  public static final SqlId LISTING = new SqlId("NUM_MEMBERS_FOR_RECENT_VISITS");
  /** Used for sorting mechanism.  */
  public static final RequestParameter SORT_ON = ReqParam.SORT_ON;
  /** Used for sorting mechanism.  */
  public static final RequestParameter ORDER = ReqParam.ORDER;
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage("Report 3", "Report3.jsp", Report3Action.class);
  private static final String DEFAULT_ORDER_BY = "ORDER BY Visit.LunchDate DESC";
  
  /** 
   Returns a List of Maps.
   
   The Map key is column name, and the map value is the column value. 
  */
  private List<Map<String, Object>> getListing() throws AppException {
    Class<?>[] targetClasses = {Date.class, Integer.class};
    
    //This style allows the date format to be specified in the JSP :
    return Report.unformatted(targetClasses, LISTING, getCustomSort());
    
    /*
    This is an alternate style, using objects that are already formatted :
    Locale locale = BuildImpl.forLocaleSource().get(getRequestParser().getRequest());
    TimeZone timeZone = BuildImpl.forTimeZoneSource().get(getRequestParser().getRequest());
    return Report.formatted(targetClasses, locale, timeZone, LISTING);
    */
  }
  
  private DynamicSql getCustomSort() throws ModelCtorException {
    DynamicSql base = getOrderBy(SORT_ON, ORDER, DEFAULT_ORDER_BY);
    return new DynamicSql(base.toString() + " LIMIT 10");
  }
}

