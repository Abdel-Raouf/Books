package hirondelle.fish.main.member;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.Collection;
import java.util.logging.Logger;

/**
 Edit the Members of the Fish and Chips Club.
 
 <P>A {@link Member} that is "inactive" will be retained in the database, but 
 will simply not be part of the RSVP list.
 
 @sql member.sql
 @view Member.jsp
*/
public final class MemberAction extends ActionTemplateListAndEdit {

  /** Constructor.  */
  public MemberAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }
  
  public static final SqlId MEMBER_LIST =  new SqlId("MEMBER_LIST");
  public static final SqlId MEMBER_FETCH =  new SqlId("MEMBER_FETCH");
  public static final SqlId MEMBER_ADD =  new SqlId("MEMBER_ADD");
  public static final SqlId MEMBER_CHANGE =  new SqlId("MEMBER_CHANGE");
  public static final SqlId MEMBER_DELETE =  new SqlId("MEMBER_DELETE");
  public static final SqlId COUNT_ACTIVE_MEMBERS =  new SqlId("COUNT_ACTIVE_MEMBERS");
  
  public static final RequestParameter IS_ACTIVE = RequestParameter.withLengthCheck("IsActive");
  public static final RequestParameter MEMBER_ID = ReqParam.ID;
  public static final RequestParameter NAME = ReqParam.NAME;
  public static final RequestParameter DISPOSITION = RequestParameter.withRegexCheck("Disposition", "(\\d)+");
  
  /** List all Members, sorted by name. */
  protected void doList() throws DAOException {
    fLogger.fine("Showing list of all members.");
    addToRequest(ITEMS_FOR_LISTING, fMemberDAO.list());
    addToRequest(NUM_ACTIVE_MEMBERS, getNumActiveMembers());
  }

  /** Ensure user input can build a {@link Member}.  */
  protected void validateUserInput() {
    fLogger.fine("Validating user input.");
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fMember = builder.build(Member.class, MEMBER_ID, NAME, IS_ACTIVE, DISPOSITION);
      fLogger.fine("New Member: " + fMember);
    }
    catch (ModelCtorException ex){
      fLogger.fine("User input cannot create a Member.");
      addError(ex);
    }    
  }
  
  /** Add a new {@link Member}.  */
  protected void attemptAdd() throws DAOException {
    fLogger.fine("Adding valid Member to database: " + fMember);
    try {
      fMemberDAO.add(fMember);
      addMessage("_1_ added successfully.", fMember.getName());
    }
    catch(DuplicateException ex){
      addError("Member Name already taken. Please use another name.");  
    }
  }
  
  /** Fetch an existing {@link Member} in order to edit it.  */
  protected void attemptFetchForChange() throws DAOException {
    Member member = fMemberDAO.fetch(getIdParam(MEMBER_ID));
    if( member == null ){
      addError("Item no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, member);
    }
  }
  
  /** Apply an edit to an existing {@link Member}.  */
  protected void attemptChange() throws DAOException {
    try {
      boolean success = fMemberDAO.change(fMember);
      if (success){
        addMessage("_1_ changed successfully.", fMember.getName());
      }
      else {
        addError("No update occurred. Item likely deleted by another user.");
      }
    }
    catch(DuplicateException ex){
      addError("Member Name already taken. Please use another name.");  
    }
  }
  
  /** 
   Delete existing {@link Member}s.  
   Deletion will fail if some other item is linked to a <tt>Member</tt>. 
  */
  protected void attemptDelete() throws DAOException {
    fLogger.fine("Attempting to delete 1 or more members.");
    Collection<Id> ids = getIdParams(MEMBER_ID);
    fLogger.fine("Member ids to be deleted: " + Util.logOnePerLine(ids));
    Integer numDeleted = fMemberDAO.deleteMany(ids);
    if( ids.size() == numDeleted ) {
      addMessage("All _1_ selected item(s) deleted successfully.", numDeleted);
    }
    else {
      addError("Only _1_ out of _2_ selected item(s) deleted successfully. Items cannot be deleted if used elsewhere.", numDeleted, ids.size());
    }
 }
  
  // PRIVATE //
  private Member fMember;
  private MemberDAO fMemberDAO = MemberDAO.getInstance();
  private static final ResponsePage FORWARD = new ResponsePage("Members", "Member.jsp", MemberAction.class);
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("MemberAction.list");
  private static final Logger fLogger = Util.getLogger(MemberAction.class);
  
  //placed in the JSP
  private static final String NUM_ACTIVE_MEMBERS = "numActiveMembers"; 

  private Integer getNumActiveMembers() throws DAOException {
    return fMemberDAO.getNumActiveMembers();
  }
}
