package hirondelle.fish.translate.locale;

import java.util.*;

import hirondelle.web4j.model.Id;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.util.Util;

/**
 Data Access Object for {@link SupportedLocale}.
*/
final class SupportedLocaleDAO {

  /** List all {@link SupportedLocale}s. */
  List<SupportedLocale> list() throws DAOException {
    return Db.list(SupportedLocale.class, SupportedLocaleAction.LOCALE_LIST);
  }
  
  /** Add a new {@link SupportedLocale}. */
  Id add(SupportedLocale aLocale) throws DAOException, DuplicateException {
    return Db.add(SupportedLocaleAction.LOCALE_ADD, baseParamsFrom(aLocale));
  }
  
  /** Fetch an existing {@link SupportedLocale}. */
  SupportedLocale fetch(Id aId) throws DAOException {
    return Db.fetch(SupportedLocale.class, SupportedLocaleAction.LOCALE_FETCH, aId);
  }
  
  /** Update an existing {@link SupportedLocale}. */
  boolean change(SupportedLocale aLocale) throws DAOException, DuplicateException {
    Object[] params = Db.addIdTo(baseParamsFrom(aLocale), aLocale.getId());
    return Util.isSuccess(Db.edit(SupportedLocaleAction.LOCALE_CHANGE, params));
  }

  /** Delete an existing {@link SupportedLocale}. */
  void delete(Id aId) throws DAOException {
    Db.delete(SupportedLocaleAction.LOCALE_DELETE, aId);
  }
  
  // PRIVATE //
  private Object[] baseParamsFrom(SupportedLocale aLocale){
    return new Object[]{aLocale.getShortForm(), aLocale.getDescription()};
  }
}
