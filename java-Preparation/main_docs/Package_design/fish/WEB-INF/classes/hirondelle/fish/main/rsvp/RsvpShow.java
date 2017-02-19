package hirondelle.fish.main.rsvp;

import hirondelle.fish.main.visit.Visit;
import hirondelle.fish.main.visit.VisitDAO;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParser;

/**
 Show listing of Active {@link hirondelle.fish.main.member.Member}s, and 
 their {@link Rsvp} response, if any. 
 
 <P>For the next future {@link Visit}, display a listing of all active 
 {@link hirondelle.fish.main.member.Member}s, 
 along with their {@link Rsvp} response, if any. Each displayed <tt>Member</tt>
 name links to a form for altering their {@link Rsvp} response.  
 
 <P>In addition, shows the total number of <tt>Member</tt>s that have responded with a 'yes'. 
 
 <P>If no future {@link Visit} exists, then none of the above applies, and nothing is displayed.
 
 @sql statements.sql RSVP_LIST_ACTIVE_MEMBERS,  RSVP_FETCH_NUM_YES,  RSVP_LIST_RESPONSES
 @view RsvpShow.jsp
*/
public final class RsvpShow extends ActionImpl {

  /** Constructor.  */
  public RsvpShow(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  public static final SqlId RSVP_LIST_ACTIVE_MEMBERS = new SqlId("RSVP_LIST_ACTIVE_MEMBERS");
  public static final SqlId RSVP_FETCH_NUM_YES = new SqlId("RSVP_FETCH_NUM_YES");
  public static final SqlId RSVP_LIST_RESPONSES = new SqlId("RSVP_LIST_RESPONSES");

  /** 
   Fetch the next future {@link Visit} (if any), along with a listing of all 
   associated {@link Rsvp} responses.  
  */
  public ResponsePage execute() throws AppException {
    VisitDAO dao = new VisitDAO();
    //might not be any future visit (possibly-null)
    Visit nextVisit = dao.fetchNextFutureVisit();
    addToRequest(NEXT_VISIT, nextVisit);

    if ( nextVisit != null ) {
      RsvpDAO rsvpDAO = new RsvpDAO();
      addToRequest(ITEMS_FOR_LISTING, rsvpDAO.list(nextVisit.getId()));
      addToRequest(NUM_YES_RESPONSES, rsvpDAO.countNumYes(nextVisit.getId()) );
    }
    return getResponsePage();
  }
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage("Rsvp", "RsvpShow.jsp", RsvpShow.class);
  private static final String NUM_YES_RESPONSES = "numYesResponses";
  private static final String NEXT_VISIT = "nextVisit"; 
}
