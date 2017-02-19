package hirondelle.fish.access.role;

import java.util.*;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.DbTx;
import hirondelle.web4j.database.Tx;
import hirondelle.web4j.database.TxTemplate;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.config.ConnectionSrc;
import java.sql.Connection;
import java.sql.SQLException;
import static hirondelle.fish.access.role.RoleAction.ROLES_ADD;
import static hirondelle.fish.access.role.RoleAction.ROLES_DELETE;
import static hirondelle.fish.access.role.RoleAction.ROLES_FETCH;
import static hirondelle.fish.access.role.RoleAction.ROLES_LIST;

final class RoleDAO {
  
  /** Return a {@code List<UserRole>} for all users, with their current roles.  */
  List<UserRole> list() throws DAOException {
    return Db.listCompound(UserRole.class, Id.class, NUM_CTOR_ARGS_CHILD, ROLES_LIST);
  }

  /** Fetch a single {@link UserRole} for a given user name.   */
  UserRole fetch(SafeText aUserName) throws DAOException {
    return Db.fetchCompound(UserRole.class, Id.class, NUM_CTOR_ARGS_CHILD, ROLES_FETCH, aUserName);
  }

  /**
   Update all roles attached to a user.
   
   <P>This implementation will treat all edits to user roles as '<tt>DELETE-ALL</tt>, then 
   <tt>ADD-ALL</tt>' operations. 
  */
  boolean change(UserRole aUserRole) throws DAOException {
    Tx update = new UpdateTransaction(aUserRole);
    return Util.isSuccess(update.executeTx());
  }
  
  // PRIVATE //
  private static final int NUM_CTOR_ARGS_CHILD = 1;
  
  /** Cannot be a {@link hirondelle.web4j.database.TxSimple}, since there is looping.  */
  private static final class UpdateTransaction extends TxTemplate {
    UpdateTransaction(UserRole aUserRole){
      super(ConnectionSrc.ACCESS_CONTROL);
      fUserRole = aUserRole;
    }
    public int executeMultipleSqls(Connection aConnection) throws SQLException, DAOException {
      int result = 0;
      result = result + DbTx.edit(aConnection, ROLES_DELETE, fUserRole.getUserName());
      for(Id roleId : fUserRole.getRoles()){
        result = result + DbTx.edit(aConnection, ROLES_ADD, fUserRole.getUserName(), roleId);
      }
      return result;
    }
    private UserRole fUserRole;
  }
}
