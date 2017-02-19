package hirondelle.web4j.config;

import hirondelle.web4j.ApplicationInfo;
import hirondelle.web4j.util.Consts;
import hirondelle.web4j.model.DateTime;


public class AppInfo implements ApplicationInfo {
  
  public String getAuthor() {
    return "Hirondelle Systems";
  }
  
  public DateTime getBuildDate() {
    return new DateTime("2013-10-19 12:28:10");
  }
  
  public String getLink() {
    return "http://www.web4j.com/";
  }
  
  public String getMessage() {
    return "Minimal Java Web Application, built with WEB4J.";
  }

  public String getName() {
    return "Electricity Tracker";
  }
  
  public String getVersion() {
    return "4.10.0.0";
  }
  
  /** Return {@link #getName()} + {@link #getVersion()}.  */
  @Override public String toString(){
    return getName() + Consts.SPACE + getVersion();
  }
}
