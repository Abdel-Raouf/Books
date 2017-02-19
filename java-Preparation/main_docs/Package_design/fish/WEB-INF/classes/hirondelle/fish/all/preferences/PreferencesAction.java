package hirondelle.fish.all.preferences;

import hirondelle.web4j.Controller;
import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.config.ConnectionSrc;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;

/**
 Update user preferences.
 
 <P>This class only changes user language preference.
 
 @sql preferences.sql
 @view Preferences.jsp
*/
public final class PreferencesAction extends ActionTemplateShowAndApply {
  
  /** Constructor. */
  public PreferencesAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }
  
  public static final SqlId FETCH_PREFERENCES = new SqlId(ConnectionSrc.ACCESS_CONTROL, "FETCH_PREFERENCES");
  public static final SqlId CHANGE_PREFERENCES = new SqlId(ConnectionSrc.ACCESS_CONTROL, "CHANGE_PREFERENCES");
  
  public static final RequestParameter LOCALE = RequestParameter.withLengthCheck("Locale");
  
  /** Show the form. */
  protected void show() throws DAOException {
    if( isLoggedIn()) {
      addToRequest(ITEM_FOR_EDIT, getPreferences());
    }
    else {
      addMessage("Please log in.");
    }
  }

  /** Check user input can build a {@link Preferences} object. */
  protected void validateUserInput() throws AppException {
    if( isLoggedIn()){
      try {
        ModelFromRequest builder = new ModelFromRequest(getRequestParser());
        fPreferences = builder.build(Preferences.class, getLoggedInUserName(), LOCALE);
      }
      catch (ModelCtorException ex){
        addError(ex);
      }
    }
    else {
      addError("Please log in."); 
    }
  }

  /** Apply updates to the database. */
  protected void apply() throws AppException {
    if( isLoggedIn() ){
      updateUserPreferences();
      eraseAnyExistingLocaleInSession();
      addMessage("Preferences updated successfully.");
    }
    else {
      addError("Please log in.");
    }
  }
  
  // PRIVATE //
  private Preferences fPreferences;
  private static final ResponsePage FORWARD = new ResponsePage("Preferences", "Preferences.jsp", PreferencesAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("PreferencesAction.show");
  
  private Preferences getPreferences() throws DAOException {
    PreferencesDAO dao = new PreferencesDAO();
    return dao.fetch(getLoggedInUserName());
  }
  
  private boolean isLoggedIn(){
    return getLoggedInUserName() != null;
  }
  
  private void updateUserPreferences() throws DAOException {
    PreferencesDAO dao = new PreferencesDAO();
    dao.change(fPreferences);
  }

  /**
   To activate the LoginTasks mechanism, the current locale, if any, needs to be removed. 
   This will immediately verify that the filter mechanism is working. 
  */
  private void eraseAnyExistingLocaleInSession(){
    addToSession(Controller.LOCALE, null);
  }
}
