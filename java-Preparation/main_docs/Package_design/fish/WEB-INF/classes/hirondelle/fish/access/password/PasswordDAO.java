package hirondelle.fish.access.password;

import hirondelle.web4j.util.Util;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.fish.access.user.User;
import static hirondelle.fish.access.password.ResetPasswordAction.RESET_PASSWORD;
import static hirondelle.fish.all.password.ChangePasswordAction.CHANGE_PASSWORD;

/**
 Data Access Object for passwords. 
*/
public final class PasswordDAO {
  
  boolean reset(User aUser) throws DAOException {
    if( ! aUser.isResetValue() ){ 
      throw new IllegalArgumentException("Password does not have the expected reset value.");
    }
    return Util.isSuccess(Db.edit(RESET_PASSWORD, aUser.getPassword(), aUser.getName()));
  }
  
  public boolean changePassword(User aOldUser, User aNewUser) throws DAOException {
    Object[] params = {aNewUser.getPassword(), aNewUser.getName(), aOldUser.getPassword()};
    return Util.isSuccess(Db.edit(CHANGE_PASSWORD, params));
  }

}
