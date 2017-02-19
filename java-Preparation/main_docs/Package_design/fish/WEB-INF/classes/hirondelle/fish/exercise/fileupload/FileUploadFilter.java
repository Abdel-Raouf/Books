package hirondelle.fish.exercise.fileupload;

import java.io.*;
import java.util.logging.*;
import hirondelle.web4j.util.Util;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 Wrap the underlying request using 
 {@link hirondelle.fish.exercise.fileupload.FileUploadWrapper}, 
 such that a file upload can be handled in the same way as a regular form.
 
 <P>The Servlet API does not treat forms which include a file upload control 
 the same as other forms. In particular, the return value for 
 <tt>request.getParameter(String)</tt> is always <tt>null</tt> for file upload forms.
 When this filter is configured for an underlying POST which includes one or more files, 
 then the application can proceed in much the same way as a regular form.
  
 <P>An exception is that the request requires a cast to the specific wrapper class 
 in order to access file-related data. Accessing 'regular' data, however, 
 proceeds normally.
 
 <P>This filter should be configured only for those operations that use a 
 file upload request.
*/
public final class FileUploadFilter implements Filter {

  public void init(FilterConfig aConfig) throws ServletException {
    //do nothing
  }
  
  public void destroy() {
    //do nothing
  }
  
  public void doFilter(
    ServletRequest aRequest, ServletResponse aResponse, FilterChain aChain
  ) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) aRequest;
    if ( isFileUploadRequest(request) ) {
      fLogger.fine("START - Wrapping request for file upload.");
      FileUploadWrapper wrapper = new FileUploadWrapper(request);
      aChain.doFilter(wrapper, aResponse);
    }
    else {
      fLogger.fine("START - NOT wrapping request for file upload.");
      aChain.doFilter(aRequest, aResponse);
    }
    fLogger.fine("Finished.");
  }
  
  private static final Logger fLogger = Util.getLogger(FileUploadFilter.class);
  
  private boolean isFileUploadRequest(HttpServletRequest aRequest){
    return     
      aRequest.getMethod().equalsIgnoreCase("POST") && 
      aRequest.getContentType().startsWith("multipart/form-data")
    ;
  }
}
