package hirondelle.fish.access.user;

import java.util.*;

import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.Tx;
import hirondelle.web4j.database.TxSimple;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.security.SafeText;
import static hirondelle.fish.access.role.RoleAction.ROLES_DELETE;
import static hirondelle.fish.access.user.UserAction.USER_LIST;
import static hirondelle.fish.access.user.UserAction.USER_ADD;
import static hirondelle.fish.access.user.UserAction.USER_DELETE;;

/**
 Data Access Object for {@link User}s. 
*/
public final class UserDAO {

  /** Used by WEB4J to protect against CSRF attacks.  */
  public static final SqlId FETCH_FORM_SOURCE_ID =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "FETCH_FORM_SOURCE_ID");
  /** Used by WEB4J to protect against CSRF attacks.  */
  public static final SqlId SAVE_FORM_SOURCE_ID =  new SqlId(ConnectionSrc.ACCESS_CONTROL, "SAVE_FORM_SOURCE_ID");
  
  /**  Return a {@link List} of all {@link User} objects. */
  public List<User> list() throws DAOException {
    return Db.list(User.class, USER_LIST);
  }

  /**
   Add a new {@link User}, with no initial roles.
   
   <P>The user name must be unique. If the name is duplicated, then a {@link DuplicateException}
   will be thrown.
  */
  void add(User aUser) throws DAOException, DuplicateException {
    Db.edit(USER_ADD, aUser.getName(), aUser.getPassword());
  }

  /**
   Delete a {@link User}, along with all related roles.
   
   <P>(This method uses a transaction.)
  */
  void delete(SafeText aUserName) throws DAOException {
    SqlId[] sqlIds = {ROLES_DELETE, USER_DELETE};
    Object[] params = {aUserName, aUserName};
    Tx deleteUserAndRoles = new TxSimple(sqlIds, params);
    deleteUserAndRoles.executeTx();
  }
}
