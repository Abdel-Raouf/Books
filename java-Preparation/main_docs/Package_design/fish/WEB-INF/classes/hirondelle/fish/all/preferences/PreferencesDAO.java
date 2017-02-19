package hirondelle.fish.all.preferences;

import static hirondelle.fish.all.preferences.PreferencesAction.CHANGE_PREFERENCES;
import static hirondelle.fish.all.preferences.PreferencesAction.FETCH_PREFERENCES;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.security.SafeText;

/**
 Data Access Object for user {@link Preferences}. 
*/
final class PreferencesDAO {
  
  /** Fetch the {@link Preferences} for a given user. */ 
  Preferences fetch(SafeText aUserName) throws DAOException {
    return Db.fetch(Preferences.class, FETCH_PREFERENCES, aUserName);
  }
  
  /** Change the {@link Preferences} for a given user. */
  void change(Preferences aPreferences) throws DAOException {
    Db.edit(CHANGE_PREFERENCES, aPreferences.getLocale().toString(), aPreferences.getUserName());
  }
}
