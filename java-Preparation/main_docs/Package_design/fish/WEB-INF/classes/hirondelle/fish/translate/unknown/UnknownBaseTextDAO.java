package hirondelle.fish.translate.unknown;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.DbTx;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Tx;
import hirondelle.web4j.database.TxTemplate;
import hirondelle.web4j.util.Util;
import hirondelle.fish.translate.basetext.BaseText;
import hirondelle.fish.translate.basetext.BaseTextAction;
import hirondelle.web4j.database.TxSimple;
import hirondelle.web4j.database.SqlId;

/**
 Data Access Object for unknown {@link BaseText}. 
 
 <P>There are two tables involved here. One for known {@link BaseText}, and 
 one for unknown <tt>BaseText</tt>.
*/
public final class UnknownBaseTextDAO {
  
  /** List all unknown {@link BaseText}. */
  List<BaseText> list() throws DAOException {
    return Db.list(BaseText.class, UnknownBaseTextEdit.LIST);
  }
  
  /** Return a count of the number of unknown {@link BaseText} items.   */
  public Integer count() throws DAOException {
    return Db.fetchValue(Integer.class, UnknownBaseTextEdit.COUNT);
  }
  
  /**
   Add all <tt>aUnknowns</tt> to the <tt>UnknownBaseText</tt> table.
   
    <P>This is an all or none operation.
  */
  public int addAll(Set<String> aUnknowns) throws DAOException {
    Tx addTx = new AddAllUnknowns(aUnknowns);
    return addTx.executeTx();
  }
  
  /**
   Delete an item from the <tt>UnknownBaseText</tt> table.
  */
  int delete(String aUnknownText) throws DAOException {
    return Db.edit(UnknownBaseTextEdit.DELETE, aUnknownText);
  }
  
  /**
   Move the given unknown to the <tt>BaseText</tt> table.
  */
  boolean move(BaseText aUnknownBaseText) throws DAOException {
    SqlId[] sqlIds = {BaseTextAction.BASE_TEXT_ADD, UnknownBaseTextEdit.DELETE};
    Object[] params = {aUnknownBaseText.getBaseText(), aUnknownBaseText.getIsCoderKey(), aUnknownBaseText.getBaseText()};
    Tx move = new TxSimple(sqlIds, params);
    return Util.isSuccess(move.executeTx());
  }
  
  // PRIVATE //
  
  /**
   Transaction having iteration over a single SQL statement.
   
   <P>A {@link TxSimple} cannot be used here, because of the iteration.
  */
  private static final class AddAllUnknowns extends TxTemplate {
    AddAllUnknowns(Set<String> aUnknowns){
      super(ConnectionSrc.TRANSLATION);
      fUnknowns = aUnknowns; 
    }
    public int executeMultipleSqls(Connection aConnection) throws SQLException, DAOException {
      int result = 0;
      for(String unknown: fUnknowns){
        addUnknown(unknown, aConnection);
        result = result + 1;
      }
      return result;
    }
    private Set<String> fUnknowns;
    private void addUnknown(String aUnknown, Connection aConnection) throws DAOException {
      DbTx.edit(aConnection, UnknownBaseTextEdit.ADD, aUnknown);
    }
  }
}
