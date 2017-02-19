package hirondelle.web4j.config;

import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import hirondelle.web4j.StartupTasks;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.database.DAOException;
import hirondelle.fish.main.codes.CodeTableUtil;
import hirondelle.web4j.Controller;

/** Implementation of {@link StartupTasks}, required by WEB4J.  */
public final class Startup implements StartupTasks {

  /** 
   Perform startup tasks.
   
   <P>Performs the following :
   <ul>
   <li>check for a version mismatch between this application and <tt>web4j.jar</tt>
   <li>look up code tables, and place them into application scope.
   <li>read in translations, and start to record 'unknown' base text. 
   See {@link TranslatorImpl} for more information.
   </ul>
  */
  public void startApplication(ServletConfig aConfig, String aDbName) throws DAOException {
    if (! Util.textHasContent(aDbName)){
      fContext = aConfig.getServletContext();
      checkForVersionMismatch();
    }
    else if (ConnectionSrc.DEFAULT.equals(aDbName)){
      lookUpCodeTablesAndPlaceIntoAppScope();
    }
    else if (ConnectionSrc.TRANSLATION.equals(aDbName)){
      fLogger.fine("Reading Translations.");
      TranslatorImpl.read();
      TranslatorImpl.startRecordingUnknowns();
    }
  }

  /**
   Called upon startup, and when the content of a pick list changes.
  
   <P>Refreshes the copies of pick lists placed in application scope.
  */
  public static void lookUpCodeTablesAndPlaceIntoAppScope() throws DAOException {
    CodeTableUtil.init(fContext);
  }
  
  // PRIVATE 
  private static ServletContext fContext;
  private static final Logger fLogger = Util.getLogger(Startup.class);
  
  /**
   This implementation depends on the exact format of two version strings.
  */
  private void checkForVersionMismatch(){
    String appVersion = getAppVersionWithoutBugFix();
    String web4jVersion = getWeb4JVersion();
    //fLogger.config("WEB4J Version: " + web4jVersion);
    AppInfo appInfo = new AppInfo();
    if ( appVersion.equals(web4jVersion)) {
      fLogger.config("Version of Fish & Chips Club (" + appInfo.getVersion() + ") compatible with that of web4j.jar (" + web4jVersion + ")");
    }
    else {
      fLogger.severe("Version of Fish & Chips Club (" + appInfo.getVersion() + ") NOT COMPATIBLE WITH THAT OF web4j.jar (" + web4jVersion + "). These versions should match exactly to ensure correct operation. Please update versions to ensure correct operation.");
    }
  }
  
  private String getAppVersionWithoutBugFix(){
    //Example base value : 3.5.0.0
    AppInfo appInfo = new AppInfo();
    int end = appInfo.getVersion().lastIndexOf(".");
    return appInfo.getVersion().substring(0,end);
    
  }
  private String getWeb4JVersion(){
    //Example base value : WEB4J/3.5.0
    int start = Controller.WEB4J_VERSION.indexOf("/");
    return Controller.WEB4J_VERSION.substring(start + 1);
  }
}
