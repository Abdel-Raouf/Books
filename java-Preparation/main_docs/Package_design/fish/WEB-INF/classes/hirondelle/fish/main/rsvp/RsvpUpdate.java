package hirondelle.fish.main.rsvp;

import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

/**
 Fetch and then change an existing {@link Rsvp}.
 
 @sql statements.sql RSVP_FETCH_FOR_CHANGE, RSVP_CHANGE
 @view RsvpUpdate.jsp
*/
public final class RsvpUpdate extends ActionTemplateShowAndApply {

  /** Constructor. */
  public RsvpUpdate(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
    fVisitId = aRequestParser.toId(RsvpAdd.VISIT_ID); 
    fMemberId = aRequestParser.toId(RsvpAdd.MEMBER_ID); 
    fRsvpDAO = new RsvpDAO();
    fLogger.fine("VisitId : " + Util.quote(fVisitId) + ", MemberId: " + Util.quote(fMemberId) );
  }

  public static final SqlId RSVP_FETCH_FOR_CHANGE = new SqlId("RSVP_FETCH_FOR_CHANGE");
  public static final SqlId RSVP_CHANGE = new SqlId("RSVP_CHANGE");
  
  //these params are shared with another action in this package
  public static final RequestParameter VISIT_ID = RsvpAdd.VISIT_ID;
  public static final RequestParameter MEMBER_ID = RsvpAdd.MEMBER_ID;
  public static final RequestParameter RESPONSE = RsvpAdd.RESPONSE;
  
  /** Fetch an existing {@link Rsvp} and place into the request.   */
  protected void show() throws AppException {
    fRsvp = fRsvpDAO.fetchForChange(fVisitId, fMemberId);
    fLogger.fine("Fetched for change : " + fRsvp);
    addToRequest(ITEM_FOR_EDIT, fRsvp);
  }
  
  /** Ensure the user has a made a Yes/No selection.   */
  protected void validateUserInput() throws AppException {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fRsvp = builder.build(Rsvp.class, VISIT_ID, MEMBER_ID, null, RESPONSE);
      //defensive only - this should never occur :
      if ( fRsvp.getResponse() == null ) {
        addError("Please make a selection.");
      }
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Update an existing {@link Rsvp}, and redirect to the listing of all RSVPs.  */
  protected void apply() throws AppException {
    fRsvpDAO.update(fRsvp);
  }
  
  // PRIVATE //
  private Id fVisitId;
  private Id fMemberId;
  private Rsvp fRsvp;
  private RsvpDAO fRsvpDAO;
  private static final ResponsePage FORWARD = new ResponsePage("Rsvp Update", "RsvpUpdate.jsp", RsvpUpdate.class);
  private static final ResponsePage REDIRECT = new ResponsePage("RsvpShow.show");
  private static final Logger fLogger = Util.getLogger(RsvpUpdate.class);
}
