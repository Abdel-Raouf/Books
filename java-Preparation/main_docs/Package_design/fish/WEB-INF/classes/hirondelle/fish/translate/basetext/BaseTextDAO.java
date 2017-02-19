package hirondelle.fish.translate.basetext;

import java.util.*;

import hirondelle.web4j.model.Id;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.util.Util;

/** Data Access Object for {@link BaseText} items.  */
public final class BaseTextDAO {
  
  /**
   Return a {@link List} of all {@link BaseText} objects, ordered alphabetically.
  */
  List<BaseText> list() throws DAOException {
    return Db.list(BaseText.class, BaseTextAction.BASE_TEXT_LIST);
  }
  
  /** Add a new {@link BaseText}. */
  Id add(BaseText aBaseText) throws DAOException, DuplicateException {
    return Db.add(BaseTextAction.BASE_TEXT_ADD, baseParamsFrom(aBaseText));
  }
  
  /** Fetch an existing {@link BaseText}. */
  public BaseText fetch(Id aId) throws DAOException {
    return Db.fetch(BaseText.class, BaseTextAction.BASE_TEXT_FETCH, aId);
  }
  
  /** Change an existing {@link BaseText}. */
  boolean change(BaseText aBaseText) throws DAOException, DuplicateException {
    Object[] params = Db.addIdTo(baseParamsFrom(aBaseText), aBaseText.getId());
    return Util.isSuccess(Db.edit(BaseTextAction.BASE_TEXT_CHANGE, params));
  }
  
  /** Delete an existing {@link BaseText}. */
  void delete(Id aId) throws DAOException {
    Db.delete(BaseTextAction.BASE_TEXT_DELETE, aId);
  }

  //PRIVATE //
  private Object[] baseParamsFrom(BaseText aBaseText){
    return new Object[]{aBaseText.getBaseText(), aBaseText.getIsCoderKey()};
  }
}
