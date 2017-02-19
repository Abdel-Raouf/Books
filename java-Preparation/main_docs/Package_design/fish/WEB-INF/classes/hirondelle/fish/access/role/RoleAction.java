package hirondelle.fish.access.role;

import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;

import java.util.ArrayList;
import java.util.List;

/**
 Edit the security Role(s) attached to users.
 
 @sql role.sql
 @view Role.jsp
*/
public final class RoleAction extends ActionTemplateListAndEdit {
  
  /** Constructor. */
  public RoleAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }
  
  public static final SqlId ROLES_FETCH =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "ROLES_FETCH");
  public static final SqlId ROLES_LIST =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "ROLES_LIST");
  public static final SqlId ROLES_DELETE =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "ROLES_DELETE");
  public static final SqlId ROLES_ADD =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "ROLES_ADD");
  
  public static final RequestParameter NAME = RequestParameter.withLengthCheck("UserName");
  public static final RequestParameter ROLES = RequestParameter.withRegexCheck(
    "Roles", "(user-general|user-president|webmaster|translator|access-control)"
  );
  
  /** List all roles for all users. */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fRoleDAO.list());
  }

  /** Build a {@link UserRole} from user input.  */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      //Need to get List explicitly here - need to use more than just a RequestParameter object :
      List<Id> roles = new ArrayList<Id>( getRequestParser().toIds(ROLES) );
      fUserRole = builder.build(UserRole.class, NAME, roles);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Not supported.  */
  protected void attemptAdd() throws DAOException {
    throw new UnsupportedOperationException();
  }
  
  /** Fetch a {@link UserRole}, in preparation for editing it. */
  protected void attemptFetchForChange() throws DAOException {
    SafeText name = getParam(NAME);
    UserRole userRole = fRoleDAO.fetch(name);
    if( userRole == null ){
      addError("Item no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, userRole);
    }
  }
  
  /** Change the roles attached to a user. */
  protected void attemptChange() throws DAOException {
    try {
      boolean success = fRoleDAO.change(fUserRole);
      if (success){
        addMessage("_1_ changed successfully.", fUserRole.getUserName());
      }
      else {
        addError("No update occurred.");
      }
    }
    catch(DuplicateException ex){
      addError("Problem occurred with possible duplicate Role.");  
    }
  }

  /** Not supported. */
  protected void attemptDelete() throws DAOException {
    throw new UnsupportedOperationException();
  }
  
  // PRIVATE //
  private UserRole fUserRole;
  private RoleDAO fRoleDAO = new RoleDAO();
  private static final ResponsePage FORWARD = new ResponsePage("Roles", "Role.jsp", RoleAction.class);
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("RoleAction.list");
}
