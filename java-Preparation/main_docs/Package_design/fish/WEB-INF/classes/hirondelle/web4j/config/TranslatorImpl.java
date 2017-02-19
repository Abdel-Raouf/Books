package hirondelle.web4j.config;

import java.util.*;
import java.util.logging.*;

import hirondelle.web4j.database.DAOException; 
import hirondelle.web4j.ui.translate.Translator;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.ui.translate.Translation;
import hirondelle.web4j.ui.translate.Translation.LookupResult;

import hirondelle.fish.translate.translation.TranslationDAO;
import hirondelle.fish.translate.unknown.UnknownBaseTextDAO;

/**
 Implementation of {@link Translator}, required by WEB4J.
 
 <P>This implementation uses a database to store translations. As part of 
 {@link hirondelle.web4j.StartupTasks}, the translations are {@link #read()}, and cached in 
 an in-memory structure, which is used to perform the actual runtime look up of translations.

 <P><span class="highlight">This class can also help to find items that need translation, 
 simply by exercising the application.</span> The steps are as follows :
 <ul>
 <li>upon startup, this class starts to "record" in memory all unknown base text items 
 (see {@link Translator} for a definition of 'base text').
 <li>the application is exercised over the desired pages. An effort is made to 
 generate all possible error messages (it is useful to perform all these tasks during regular unit testing).
 <li>{@link #stopRecordingUnknowns} is then called. This class will stop recording unknowns,
 and flush all existing unknowns to the database.
 <li>the screens provided in the <tt>translate</tt> module are used to evaluate each item : add it as 
 a natural language key, add it as a coder key, or delete it from further consideration. 
 <li>{@link #read} is called to refresh the in-memory translations. 
 <li>one may then call call {@link #startRecordingUnknowns} to repeat the process. 
 </ul>
 
 <P>The above can be performed both during application development and during production, 
 to find items that may have been missed. Screens are provided for all of the above tasks.
 
 <P><span class="highlight">The start/stop action for recording unknowns is global, and applies to all users.</span> 
 If more than one person is simultaneously working on finding and evaluating unknown base text, 
 then they should coordinate their efforts, to know when recording is on/off. 
*/
public final class TranslatorImpl implements Translator {  

  /** 
   Look up all translations from a database, and store them in a static member.
   
   <P>Must be called by {@link hirondelle.web4j.StartupTasks}. May also be called 
   after startup, to refresh the data.
  */
  public static synchronized void  read() throws DAOException {
    TranslationDAO dao = new TranslationDAO();
    fTranslations = dao.getTranslations();
    fLogger.finest("Fetched Translations : " + Util.logOnePerLine(fTranslations));
  }

  /** Return the number of translations.  */
  public static synchronized Integer getNumTranslations(){
    return fTranslations.size();
  }
  
  /**
   Start recording unknown base text.
   
   <P>Recording can start only if : 
   <ul>
   <li>this class is not already recording.
   <li>there are no entries in the database for unknown base text. That is, starting to record can only be done after all
   unknown base text items already persisted to the database have been evaluated and processed, thus removing them 
   from the unknowns 'queue'. 
   </ul>
   
   <P>Items are recorded to an in-memory structure only. They are saved to the database by 
   calling {@link #stopRecordingUnknowns()}. 
  */
  public static synchronized void startRecordingUnknowns() throws DAOException {
    if( fIsRecording ){
      throw new IllegalArgumentException("Recording of Unknowns has already started.");
    }
    
    if( numPersistedUnknownEntries() == 0) {
      fLogger.fine("Starting to record Unknown Base Text in memory, for later persistence.");
      fIsRecording = true;
      fUnknownBaseText.clear();
    }
    else {
      fLogger.fine("Cannot start recording. Must have 0 entries in Unknown Base Text table before recording can start.");
      fIsRecording = false;
    }
  }
  
  /**
   Stop recording of unknown base text, and persist unknown items to the database.
   
   <P>This method fails if this class is not already recording.
  */
  public static synchronized void stopRecordingUnknowns() throws DAOException {
    if( ! fIsRecording ){
      throw new IllegalArgumentException("Recording of Unknowns has not yet started.");
    }
    fLogger.fine("Stop recording of Unknown Base Text. Save to database, and clear in-memory cache of recorded items.");
    storeUnknownsInTable();
    fUnknownBaseText.clear();
    fIsRecording = false;
  }

  /** Return <tt>true</tt> only if this class is currently recording unknown base text. */
  public static synchronized boolean isRecording(){
    return fIsRecording;
  }
   
  /**
   Look up the translation for <tt>aBaseText</tt> and <tt>aLocale</tt>. 
    
   <P>If <tt>aBaseText</tt> is not known, or if there is no explicit translation for that 
   {@link Locale}, then return <tt>aBaseText</tt> as is.
   
   <P>If <tt>aBaseText</tt> is not known, and this class is 'recording', then it is added to the 
   unknown items.
  */
  public String get(String aBaseText, Locale aLocale) {
    String result = null;
    LookupResult lookup = Translation.lookUp(aBaseText, aLocale, fTranslations);
    if( lookup.hasSucceeded() ){ 
      result = lookup.getText();
    }
    else {
      result = aBaseText;
      if(LookupResult.UNKNOWN_BASE_TEXT == lookup){
        addUnknown(aBaseText);
      }
      else if (LookupResult.UNKNOWN_LOCALE == lookup){
        //do nothing in this implementation; since the base text exists, any "missing" translations
        //for a given locale can always be retrieved by an ordinary query
      }
    }
    fLogger.finest("Translation of " + Util.quote(aBaseText) + " for Locale " + aLocale + ": " + Util.quote(result));
    return result;
  }
  
  // PRIVATE //
  
  /**
   Holds all translations in memory, as a  
   Map[BaseText, Map[Locale, Translation]].
   
   (Empty object provided here; without an object, you will have an unnecessary NullPointerException 
   if the database is down upon startup.) 
  */
  private static Map<String, Map<String, String>> fTranslations = new HashMap<String, Map<String, String>>();
  
  /**  On/off indicator for recording.  */
  private static boolean fIsRecording;
  
  /** In-memory store of base text items that are currently unknown to fTranslations.  */
  private static final Set<String> fUnknownBaseText = new HashSet<String>();
  
  private static final Logger fLogger = Util.getLogger(TranslatorImpl.class);
  
  private static synchronized void addUnknown(String aBaseText){
    if (fIsRecording){
      fUnknownBaseText.add(aBaseText);
    }
  }
  
  private static synchronized void storeUnknownsInTable() throws DAOException {
    UnknownBaseTextDAO dao = new UnknownBaseTextDAO();
    dao.addAll(fUnknownBaseText);
  }
  
  private static synchronized int numPersistedUnknownEntries() throws DAOException {
    UnknownBaseTextDAO dao = new UnknownBaseTextDAO();
    return dao.count().intValue();
  }
}
