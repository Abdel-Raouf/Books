package hirondelle.electricity.main.home;

import java.util.*;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.util.Util;
import static hirondelle.electricity.main.home.SpendingAction.LIST_SPENDING;
import static hirondelle.electricity.main.home.SpendingAction.FETCH_SPENDING;
import static hirondelle.electricity.main.home.SpendingAction.ADD_SPENDING;
import static hirondelle.electricity.main.home.SpendingAction.CHANGE_SPENDING;
import static hirondelle.electricity.main.home.SpendingAction.DELETE_SPENDING;

final class SpendingDAO {
  
  List<Spending> list() throws DAOException {
     return Db.list(Spending.class, LIST_SPENDING);    
  }
  
  Spending fetch(Id aId) throws DAOException {
    return Db.fetch(Spending.class, FETCH_SPENDING, aId);
  }
  
  void add(Spending aSpending) throws DAOException {
    Db.add(ADD_SPENDING, baseParamsFrom(aSpending));
  }
  
  boolean change(Spending aSpending) throws DAOException {
    Object[] params = Db.addIdTo(baseParamsFrom(aSpending), aSpending.getId()); 
    return Util.isSuccess(Db.edit(CHANGE_SPENDING, params));    
  }
  
  void delete(Id aId) throws DAOException {
    Db.delete(DELETE_SPENDING, aId);
  }

  // PRIVATE 
  
  private Object[] baseParamsFrom(Spending aSpending){
    return new Object[]{ 
      aSpending.getDatePaid(), aSpending.getPayment(), aSpending.getKilowattHours(), 
      aSpending.getIsEstimated(), aSpending.getFacility().getId(), aSpending.getComment()
    };
  }
}
