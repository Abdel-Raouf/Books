package hirondelle.fish.main.home;

import hirondelle.fish.main.visit.Visit;
import hirondelle.fish.main.visit.VisitDAO;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParser;

/**
 Show the Fish and Chips Club home page.
 
 <P>The home page shows the next future {@link Visit}, if any, with a link for 
 Members to declare their intentions to attend or not attend.
 
 <P><b>Summary</b>
 <ul>
 <li>Operation performed : {@link VisitDAO#fetchNextFutureVisit()},
 </ul>
 
 @view Home.jsp
*/
public final class HomePageAction extends ActionImpl {
  
  /** Constructor.  */
  public HomePageAction(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** 
   Show the home page, with info regarding the next future {@link Visit}, if any.
     
   <P> See {@link VisitDAO#fetchNextFutureVisit()}.  
  */
  public ResponsePage execute() throws AppException {
    VisitDAO dao = new VisitDAO();
    //might not be any future visit (possibly-null)
    addToRequest(NEXT_VISIT, dao.fetchNextFutureVisit());
    return getResponsePage();
  }
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage(
    "Home", "Home.jsp", HomePageAction.class
  );
   
  /**  Value {@value} - id for {@link Visit} used in JSP.   */
  private static final String NEXT_VISIT = "nextVisit";
}
