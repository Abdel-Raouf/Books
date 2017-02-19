package hirondelle.fish.access.password;

import hirondelle.fish.access.user.User;
import hirondelle.fish.access.user.UserDAO;
import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;

/**
 Reset the user's password.
 
 <P>The password is reset to a conventional value. 
 See {@link User#forNewUserOrPasswordReset} for more information.
 
 @sql password.sql RESET_PASSWORD
 @view ResetPassword.jsp
*/
public final class ResetPasswordAction extends ActionTemplateShowAndApply {

  /** Constructor. */
  public ResetPasswordAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }
  
  public static final SqlId RESET_PASSWORD =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "RESET_PASSWORD");
  public static final RequestParameter USER_NAME = RequestParameter.withLengthCheck("UserName");

  /** List all user names. */
  protected void show() throws DAOException {
    UserDAO dao = new UserDAO();
    addToRequest(ITEMS_FOR_LISTING, dao.list());
  }
  
  /** Create a new version of {@link User} with a reset password. See {@link User#forNewUserOrPasswordReset(SafeText)}. */
  protected void validateUserInput() {
    try {
      fUser = User.forNewUserOrPasswordReset(getParam(USER_NAME));
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }

  /** Apply the edit to the database.  */
  protected void apply() throws DAOException {
    PasswordDAO dao = new PasswordDAO();
    boolean success = dao.reset(fUser);
    if (success){
      addMessage("Password for _1_ reset successfully.", fUser.getName());
    }
    else {
      addError("No update occurred. Item likely deleted by another user.");
    }
  }

  // PRIVATE //
  private User fUser; //build only with 'standard', fixed password.
  private static final ResponsePage FORWARD = new ResponsePage("Password Reset", "ResetPassword.jsp", ResetPasswordAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("ResetPasswordAction.show");
}
  
