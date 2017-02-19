package hirondelle.web4j.config;

import hirondelle.web4j.ApplicationInfo;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.util.Consts;

/**
 Implementation of {@link ApplicationInfo}, required by WEB4J.
*/
public final class AppInfo implements ApplicationInfo {
  
  public String getVersion(){    
    return "4.10.0.0";  
  }
  
  public DateTime getBuildDate(){
    return new DateTime("2014-10-30 13:59:51");
  }
  
  public String getName(){
    return "Fish And Chips Club";
  }
  
  public String getAuthor(){
    return "Hirondelle Systems";
  }
  
  public String getLink(){
    return "http://www.web4j.com/";
  }
  
  public String getMessage(){
    return "Web4j example application.";
  }

  /** Return {@link #getName()} + {@link #getVersion()}. */
  @Override public String toString(){
    return getName() + Consts.SPACE + getVersion();
  }
}
