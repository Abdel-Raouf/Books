package hirondelle.fish.all.password;

import hirondelle.fish.access.password.PasswordDAO;
import hirondelle.fish.access.user.User;
import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

/**
 Change the password.
 
 @sql ../../access/password/password.sql CHANGE_PASSWORD, CHANGE_XXX, CHANGE_YYY
 @view ChangePassword.jsp
*/
public final class ChangePasswordAction extends ActionTemplateShowAndApply {
  
  /** Constructor. */
  public ChangePasswordAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }
  
  static public final SqlId CHANGE_PASSWORD =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "CHANGE_PASSWORD");
  
  public static final RequestParameter NAME = RequestParameter.withLengthCheck("UserName");
  public static final RequestParameter OLD_PASSWORD = RequestParameter.withLengthCheck("Old Password");
  public static final RequestParameter NEW_PASSWORD = RequestParameter.withLengthCheck("New Password");
  public static final RequestParameter CONFIRM_NEW_PASSWORD = RequestParameter.withLengthCheck("Confirm New Password");

  /** Show the form for changing the password. */
  protected void show() throws DAOException {
    if( ! Util.textHasContent(getLoggedInUserName().getRawString()) ){
      addError("Please log in.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, getLoggedInUserName());
    }
  }
  
  /**
   Validate that both the old and new passwords are of valid form.
   
   <P>Also validates that the user is still logged in (just in case).
  */
  protected void validateUserInput() {
    //this method is unusual since more than one Model Object is constructed.
    SafeText userName = getLoggedInUserName();
    if( ! Util.textHasContent(userName.getRawString()) ){
      addError("Please log in.");
    }
    else {
      try {
        fOldUser = getUserFor(userName, OLD_PASSWORD);
        fNewUser = getUserFor(userName, NEW_PASSWORD);
        fConfirmNewUser = getUserFor(userName, CONFIRM_NEW_PASSWORD);
        if( ! fConfirmNewUser.getPassword().equals(fNewUser.getPassword()) ){
          addError("Please try again. New password not confirmed as expected.");
        }
      }
      catch (ModelCtorException ex){
        addError(ex);
      }    
    }
  }

  /** Apply the change to the database.  */
  protected void apply() throws DAOException {
    //Change requires both the new and the old.
    PasswordDAO dao = new PasswordDAO();
    boolean success = dao.changePassword(fOldUser, fNewUser);
    if (success){
      addMessage("Password for _1_ updated successfully.", fNewUser.getName());
    }
    else {
      addError("Password not updated. Please verify old password.");
    }
  }
  
  // PRIVATE //
  private User fOldUser; 
  private User fNewUser; 
  private User fConfirmNewUser;
  
  private static final ResponsePage FORWARD = new ResponsePage("Change Password", "ChangePassword.jsp", ChangePasswordAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("ChangePasswordAction.show");

  private User getUserFor(SafeText aUserName, RequestParameter aPassword) throws ModelCtorException {
    SafeText password = getParam(aPassword);
    return User.forPasswordChange(aUserName, password);
  }
}
