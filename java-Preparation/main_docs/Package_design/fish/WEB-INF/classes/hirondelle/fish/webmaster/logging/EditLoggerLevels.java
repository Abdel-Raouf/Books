package hirondelle.fish.webmaster.logging;

import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Consts;
import hirondelle.web4j.util.Util;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 List all JRE loggers, and allow changes to logging levels. 
 
 @view view.jsp
*/
public final class EditLoggerLevels extends ActionTemplateListAndEdit {

  /** Constructor. */
  public EditLoggerLevels(RequestParser aRequestParser) {
    super(FORWARD, REDIRECT, aRequestParser);
  }

  public static final RequestParameter LOGGER_NAME = RequestParameter.withLengthCheck("Name");
  public static final RequestParameter LEVEL = RequestParameter.withRegexCheck("Level",
    "(ALL|INHERIT FROM PARENT|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|OFF)");

  /** List all JRE {@link Logger}s. */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, getLoggerNamesAndLevels());
  }

  /** Ensure user input makes sense. */
  protected void validateUserInput() {
    fLoggerName = getParamUnsafe(LOGGER_NAME);
    if ( ! Util.textHasContent(fLoggerName) ) {
      fLoggerName = ROOT_LOGGER;
    }
    String loggerLevel = getParamUnsafe(LEVEL);
    if (!Util.textHasContent(loggerLevel)) {
      addError("Logger Level must be specified");
    }
    else {
      if ("INHERIT FROM PARENT".equals(loggerLevel)) {
        fLevel = null;
      }
      else {
        try {
          fLevel = Level.parse(loggerLevel);
        }
        catch (IllegalArgumentException ex) {
          addError("Cannot parse input into a valid Level.");
        }
      }
    }
  }

  /** Fetch a {@link Logger} in preparation for editing its level. */
  protected void attemptFetchForChange() throws DAOException {
    String loggerName = getParamUnsafe(LOGGER_NAME);
    if (loggerName == null) {
      loggerName = ROOT_LOGGER;
    }
    LogManager manager = LogManager.getLogManager();
    Logger logger = manager.getLogger(loggerName);
    if (logger != null) {
      addToRequest(ITEM_FOR_EDIT, logger);
    }
  }

  /** Update the logging level for a single {@link Logger}. */
  protected void attemptChange() throws DAOException {
    LogManager manager = LogManager.getLogManager();
    Logger logger = manager.getLogger(fLoggerName);
    logger.setLevel(fLevel);
  }

  /** Not supported. */
  protected void attemptAdd() throws DAOException {
    throw new UnsupportedOperationException();
  }

  /** Not supported. */
  protected void attemptDelete() throws DAOException {
    throw new UnsupportedOperationException();
  }

  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage("Loggers", "view.jsp", EditLoggerLevels.class);
  private static final ResponsePage REDIRECT = new ResponsePage("EditLoggerLevels.do?Operation=List");
  private String fLoggerName;
  private Level fLevel;
  private static final String ROOT_LOGGER = Consts.EMPTY_STRING;

  private Map<String, Logger> getLoggerNamesAndLevels() {
    Map<String, Logger> result = new TreeMap<String, Logger>(String.CASE_INSENSITIVE_ORDER); // sorts alphabetically
    LogManager logManager = LogManager.getLogManager();
    Enumeration loggerNames = logManager.getLoggerNames();
    while (loggerNames.hasMoreElements()) {
      String loggerName = (String)loggerNames.nextElement();
      Logger logger = logManager.getLogger(loggerName);
      result.put(loggerName, logger);
    }
    return result;
  }
}
