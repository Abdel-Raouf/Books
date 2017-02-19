package hirondelle.fish.access.user;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;

/**
 Edit {@link User} objects.
 
 @sql user.sql
 @view User.jsp
*/
public final class UserAction extends ActionTemplateListAndEdit {

  /** Constructor. */
  public UserAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }

  public static final SqlId USER_LIST =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "USER_LIST");
  public static final SqlId USER_ADD =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "USER_ADD");
  public static final SqlId USER_DELETE =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "USER_DELETE");
  
  public static final RequestParameter USER_NAME = ReqParam.NAME;

  /** List all users. */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fUserDAO.list());
  }

  /** Create a {@link User} from user input. */
  protected void validateUserInput() {
    try {
      SafeText userName = getParam(USER_NAME);
      fUser = User.forNewUserOrPasswordReset(userName);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Add a new {@link User}.  */
  protected void attemptAdd() throws DAOException {
    try {
      fUserDAO.add(fUser);
      addMessage("_1_ added successfully.", fUser.getName());
    }
    catch(DuplicateException ex){
      addError("User Name already taken. Please try a different name.");  
    }
  }
  
  /** Not supported. */
  protected void attemptFetchForChange() throws DAOException {
    throw new UnsupportedOperationException();
  }
  
  /** Not supported. */
  protected void attemptChange() throws DAOException {
    throw new UnsupportedOperationException();
  }

  /** Delete a {@link User}. */
  protected void attemptDelete() throws DAOException {
    try {
      fUserDAO.delete(getParam(USER_NAME));
      addMessage("Item deleted successfully.");
    }
    catch (DAOException ex) {
      addError("Cannot delete. Item likely used elsewhere.");      
    }
  }
  
  // PRIVATE //
  private User fUser;
  private UserDAO fUserDAO = new UserDAO();
  private static final ResponsePage FORWARD = new ResponsePage("Users", "User.jsp", UserAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("UserAction.list");
}
    
