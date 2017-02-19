package hirondelle.fish.translate.translation;

import java.util.*;

import hirondelle.fish.translate.basetext.BaseText;
import hirondelle.fish.translate.locale.SupportedLocale;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.ui.translate.Translation; 
import hirondelle.web4j.model.Id;
import hirondelle.web4j.config.ConnectionSrc;
import static hirondelle.fish.translate.translation.TranslationEdit.ADD;
import static hirondelle.fish.translate.translation.TranslationEdit.CHANGE;
import static hirondelle.fish.translate.translation.TranslationEdit.DELETE;
import static hirondelle.fish.translate.translation.TranslationEdit.FETCH;
import static hirondelle.fish.translate.translation.TranslationEdit.LIST;

/**
 Data Access Object for translations. 
 
 <P>This DAO is <tt>public</tt> since it is referenced from another package.
 
 <P>Operations performed :
 {@link #FETCH_FROM_CODER_KEYS},
 {@link #FETCH_FROM_NATURAL_LANG_1},
 {@link #FETCH_FROM_NATURAL_LANG_2} in <a href='translation.sql'>SQL Statements</a>. 
*/
public final class TranslationDAO {
  
  public static final SqlId FETCH_FROM_CODER_KEYS = getSqlId("FETCH_TRANSLATIONS_FROM_CODER_KEYS");
  /*
   Important Note : 
   This separation into ONE + TWO exists only because the target db used in dev has no UNION operation.
   If the target database used in dev was more mature, then a much more compact implementation would be possible. 
  */
  public static final SqlId FETCH_FROM_NATURAL_LANG_1 = getSqlId("FETCH_TRANSLATIONS_FROM_NATURAL_LANGUAGE_KEYS_ONE");
  public static final SqlId FETCH_FROM_NATURAL_LANG_2 = getSqlId("FETCH_TRANSLATIONS_FROM_NATURAL_LANGUAGE_KEYS_TWO");
  
  /**
   Return Map[BaseText, Map[Locale, Translation]].
   
   <P>Calls {@link Translation#asNestedMap(java.util.Collection)}.
  */
  public Map<String, Map<String, String>> getTranslations() throws DAOException {
    return Translation.asNestedMap(list());
  }

  /**
   Return a {@link List} of all {@link Translation} objects.
   
   <P>Order by base text, then by {@link Locale}.
  */
  List<Translation> list() throws DAOException {
    List<Translation> transForCoderKeys = Db.list(Translation.class, FETCH_FROM_CODER_KEYS);
    List<Translation> transForLangOne = Db.list(Translation.class, FETCH_FROM_NATURAL_LANG_1);
    List<Translation> transForLangTwo = Db.list(Translation.class, FETCH_FROM_NATURAL_LANG_2);
    
    //remove possible dupes 
    Set<Translation> noDupes = new LinkedHashSet<Translation>();
    noDupes.addAll(transForCoderKeys);
    noDupes.addAll(transForLangOne);
    noDupes.addAll(transForLangTwo);
    List<Translation> result = new ArrayList<Translation>(noDupes);
    Collections.sort(result);
    return result;
  }
  
  /**
   Return a {@code List<Translation>}, containing all translations for the given <tt>BaseTextId</tt>.
   <P>A translation for the base language (English) is returned only if the {@link BaseText} is a coder key.
  */
  List<Translation> listFor(Id aBaseTextId) throws DAOException {
    return Db.list(Translation.class, LIST, aBaseTextId);
  }
  
  /**
   Add a new {@link Translation}.
   
   <P>For a given {@link BaseText}, there can be only one translation for a given {@link SupportedLocale}.
  */
  void add(Translation aTranslation) throws DAOException, DuplicateException {
    Object[] params = {aTranslation.getBaseTextId(), aTranslation.getLocaleId(), aTranslation.getTranslation()};
    Db.edit(ADD, params);
  }
  
  /**
   Return a single {@link Translation}.
   
   <P>No {@link Translation} is returned if {@link BaseText} has a natural language key, and 
   the <tt>Locale</tt> is the base locale.
  */
  Translation fetch(Id aBaseTextId, Id aLocaleId) throws DAOException {
    return Db.fetch(Translation.class, FETCH, aBaseTextId, aLocaleId);
  }
  
  /**
   Update the text of a {@link Translation}.
   
   <P>The <tt>Locale</tt> of the {@link Translation} is not altered by this method. 
  */
  void change(Translation aTranslation) throws DAOException {
    Object[] params = {aTranslation.getTranslation(), aTranslation.getBaseTextId(), aTranslation.getLocaleId()};
    Db.edit(CHANGE, params);
  }
  
  /** Delete an exiting {@link Translation}. */
  void delete(Id aBaseTextId, Id aLocaleId) throws DAOException {
    Db.edit(DELETE, aBaseTextId, aLocaleId);
  }
  
  // PRIVATE //
  private static SqlId getSqlId(String aSqlId){
    return new SqlId(ConnectionSrc.TRANSLATION, aSqlId);
  }
}
