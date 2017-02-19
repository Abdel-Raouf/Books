package hirondelle.fish.exercise.binary;

import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.request.RequestParser;
import java.io.IOException;
import java.util.logging.*;
import hirondelle.web4j.util.Util;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 Serves a simple binary file, in <tt>.rtf</tt> format.
  
 <P>Exercises serving a binary, non-HTML response to the user.
 This class generates a fixed response. In practice, it is almost always the case that 
 the content will be generated dynamically, using the database. 
 
 <P>This action is activated by a URL that ends in <tt>.rtf</tt>. This helps the browser confirm the  
 content-type, and to open the appropriate helper application.
 
 See <a href='http://search.cpan.org/~sburke/RTF-Writer-1.11/lib/RTF/Cookbook.pod'>RTF Cookbook</a>
 for more information on <tt>.rtf</tt> files.
 
 <P>See the fileupload package for a more practical example of handling binary data.
*/
public final class ServeBinaryAction extends ActionImpl {

  public ServeBinaryAction(RequestParser aRequestParser) {
    super(RTF_REPORT, aRequestParser);
  }

  /** Serve a simple <tt>.rtf</tt> file, having fixed content.  */
  @Override  public ResponsePage execute() throws AppException {
    fLogger.fine("Serving response as an .rtf file.");
    HttpServletResponse response = getRequestParser().getResponse();
    try {
      //you need to use call response.getWriter() (text) OR response.getOutputStream() (binary)
      PrintWriter out = response.getWriter();
      try {
        //set the content-type response header accordingly
        //one might set this instead in web.xml, using mime-mapping.
        response.setContentType("application/rtf");
        //output some fixed content in RTF file format
        out.println(
          "{\\rtf1\\ansi\\deff0{\\fonttbl{\\f0 Times New Roman;}}" + 
          "{\\pard\\fs32\\b NOTE\\par}" +
          "{\\pard\\fs26 Bangalore is rather {\\i hot and sticky} " +
          "at this time of year, don't you think? " +
          "\\line But some people {\\b rather like it} that way..." +
          "\\par}" +
          "}"
        ); 
      }
      finally {
        //close streams, clean up resources
        out.close();
      }
    }
    catch (IOException ex) {
      throw new AppException("Cannot generate binary output.", ex);
    }
    return getResponsePage();
  }

  // PRIVATE 
  private static final ResponsePage RTF_REPORT = ResponsePage.withBinaryData();
  private static final Logger fLogger = Util.getLogger(ServeBinaryAction.class);
}
