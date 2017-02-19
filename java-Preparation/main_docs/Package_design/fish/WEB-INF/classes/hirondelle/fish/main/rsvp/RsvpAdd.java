package hirondelle.fish.main.rsvp;

import hirondelle.fish.main.member.Member;
import hirondelle.fish.main.member.MemberDAO;
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
 Add an {@link Rsvp} for a particular {@link Member}.
 
 @sql statements.sql RSVP_ADD
 @view RsvpAdd.jsp
*/
public final class RsvpAdd extends ActionTemplateShowAndApply {

  /** Constructor.  */
  public RsvpAdd(RequestParser aRequestParser) {
    super(FORWARD, REDIRECT, aRequestParser);
    fVisitId = aRequestParser.toId(VISIT_ID);
    fMemberId = aRequestParser.toId(MEMBER_ID);
    fLogger.fine("VisitId : " + Util.quote(fVisitId) + ", MemberId: " + Util.quote(fMemberId));
  }

  public static final SqlId RSVP_ADD = new SqlId("RSVP_ADD");

  public static final RequestParameter VISIT_ID = RequestParameter.withLengthCheck("VisitId");
  public static final RequestParameter MEMBER_ID = RequestParameter.withLengthCheck("MemberId");
  public static final RequestParameter RESPONSE = RequestParameter.withLengthCheck("Response");

  /** Show the form for adding a new {@link Rsvp}.  */ 
  protected void show() throws AppException {
    addToRequest(ITEM_FOR_EDIT, useStubRsvp());
  }

  /** Ensure user input can build an {@link Rsvp}. */
  protected void validateUserInput() throws AppException {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fRsvp = builder.build(Rsvp.class, VISIT_ID, MEMBER_ID, null, RESPONSE);
      if (fRsvp.getResponse() == null) {
        // this is not actually exercised, since the selection is coerced to 'false' by
        // RequestParser.
        addError("Please make a Yes/No selection");
      }
    }
    catch (ModelCtorException ex) {
      // defensive - should not happen
      addError("Unexpected error - cannot construct Rsvp object.");
    }
  }

  /** Add a new {@link Rsvp} to the database. */
  protected void apply() throws AppException {
    RsvpDAO dao = new RsvpDAO();
    dao.add(fRsvp);
  }

  // PRIVATE //
  private Id fVisitId;
  private Id fMemberId;
  private Rsvp fRsvp;

  private static final ResponsePage FORWARD = new ResponsePage("Rsvp Add", "RsvpAdd.jsp",RsvpAdd.class);
  private static final ResponsePage REDIRECT = new ResponsePage("RsvpShow.show");
  private static final Logger fLogger = Util.getLogger(RsvpAdd.class);

  /**
   Return a "stub" object, that carries database ids, but no actual user input. This stub
   object can be used in the JSP, in the web4j prepopulate mechanism. Thus, there is kind
   of a mixture of the 'add' and 'change' use cases here.
  */
  private Rsvp useStubRsvp() throws AppException {
    MemberDAO dao = new MemberDAO();
    Member member = dao.fetch(fMemberId);
    return new Rsvp(fVisitId, fMemberId, member.getName(), null);
  }
}
