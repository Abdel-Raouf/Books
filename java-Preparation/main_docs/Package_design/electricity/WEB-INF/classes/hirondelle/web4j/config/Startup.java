package hirondelle.web4j.config;

import javax.servlet.ServletConfig;
import hirondelle.web4j.StartupTasks;
import hirondelle.web4j.model.AppException;
import hirondelle.electricity.codes.CodeTableUtil;

public class Startup implements StartupTasks {

  /** Extract code table data and place in memory.  */
  public void startApplication(ServletConfig aConfig, String aDbName) throws AppException {
    if (ConnectionSrc.DEFAULT.equals(aDbName)){
      CodeTableUtil.init(aConfig.getServletContext());
    }
  }

}
