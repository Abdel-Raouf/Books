package hirondelle.fish.main.search;

import static hirondelle.web4j.database.DynamicSql.AND;
import static hirondelle.web4j.database.DynamicSql.DESC;
import static hirondelle.web4j.database.DynamicSql.WHERE;
import static hirondelle.web4j.util.Consts.NEW_LINE;
import hirondelle.fish.main.resto.Resto;
import hirondelle.web4j.action.ActionTemplateSearch;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.DynamicSql;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 Search for {@link Resto}s by name. 
 
 <P>There are two search criteria, one on name, and one on price.
 
 <P>As well, the sort order of the listing may also be reversed. 
 
<P> <em>Implementation Note:</em><br> 
 The <tt>WHERE</tt> and <tt>ORDER BY</tt> clauses of the SQL statement 
 are dynamically generated using {@link hirondelle.web4j.database.DynamicSql}.
 
 @sql statements.sql
 @view view.jsp
*/
public final class RestoSearchAction extends ActionTemplateSearch {

  /** Constructor. */
  public RestoSearchAction(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  public static final RequestParameter STARTS_WITH = RequestParameter.withLengthCheck("Starts With");
  public static final RequestParameter REVERSE_SORT = RequestParameter.withRegexCheck("Reverse Sort", "(true)");
  public static final RequestParameter MIN_PRICE = RequestParameter.withLengthCheck("Minimum Price");
  public static final RequestParameter MAX_PRICE = RequestParameter.withLengthCheck("Maximum Price");
  public static final RequestParameter ORDER_BY = RequestParameter.withLengthCheck("Order By");
  
  public static final SqlId RESTO_DYNAMIC_SEARCH = new SqlId("RESTO_DYNAMIC_SEARCH");
  
  /** 
   Ensure the user input can build a {@link RestoSearchCriteria}.
   
   <P>The end user may input criteria on name, price or both. The {@link #ORDER_BY} parameter is always submitted, 
   since there is no 'blank' value in the <tt>SELECT</tt> control. 
  */
  protected void validateUserInput() throws AppException {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fCriteria = builder.build(RestoSearchCriteria.class, STARTS_WITH, MIN_PRICE, MAX_PRICE, ORDER_BY, REVERSE_SORT);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }
  }
  
  /** Fetch the records consistent with input criteria, and display the result. */
  protected void listSearchResults() throws AppException {
    //note that BOTH the criteria AND the corresponding '?' data need to be calculated 'simultaneously': they are 
    //closely related, since the data passed in depends tightly on which criteria are present
    List restos = Db.search(Resto.class, RESTO_DYNAMIC_SEARCH, getCriteria(), getParams());
    if ( restos.isEmpty() ){
      addMessage("No Results.");
    }
    else {
      addToRequest(ITEMS_FOR_LISTING, restos);
    }
  }
  
  // PRIVATE 
  private static final ResponsePage FORWARD = new ResponsePage("Search", "view.jsp", RestoSearchAction.class);
  private RestoSearchCriteria fCriteria;
  private static final String NAME_LIKE = "Name Like ?" + NEW_LINE;
  private static final String PRICE_RANGE = "Price >= ? AND Price <= ?" + NEW_LINE;
  private static final Logger fLogger = Util.getLogger(RestoSearchAction.class);
  
  /* Could move this method into a DAO, if desired. */
  private DynamicSql getCriteria() throws ModelCtorException {
    StringBuilder result = new StringBuilder(WHERE + NEW_LINE);
    boolean hasAddedFirstCriterion = false;
    
    if( Util.textHasContent(fCriteria.getStartsWith()) ) {
      result.append(NAME_LIKE);
      hasAddedFirstCriterion = true;
    }
    if( fCriteria.getMinPrice() != null ){
      if( hasAddedFirstCriterion ){
        result.append(AND);
      }
      result.append(PRICE_RANGE);
    }
    
    result.append(DynamicSql.ORDER_BY + fCriteria.getOrderBy());
    if (fCriteria.isReverseOrder()) {
      result.append(DESC);
    }
    
    fLogger.fine("Dynamic SQL criteria: " + result);
    return new DynamicSql(result);
  }
  
  /**
   Return the user input values for all '?' placeholders. 
   
   <P>Since the underlying SQL is dynamic, the exact sequence of params depends on the selected criteria. 
   Different Object sequences are needed for different groups of criteria. 
  */
  private Object[] getParams(){
    List<Object> result = new ArrayList<Object>();
    if( Util.textHasContent(fCriteria.getStartsWith()) ){
      result.add(fCriteria.getStartsWith());
    }
    if ( fCriteria.getMinPrice() != null ) {
      result.add( fCriteria.getMinPrice() );
      result.add( fCriteria.getMaxPrice() );
    }
    return result.toArray();
  }
}
