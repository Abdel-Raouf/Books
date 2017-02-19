package hirondelle.fish.exercise.fileupload;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 Fetch an image from the database, and serve it directly to the client, without using a JSP.
*/
public final class ImageDownloadAction extends ActionImpl {
  
  public ImageDownloadAction(RequestParser aRequestParser) {
    super(SERVE_IMAGE, aRequestParser);
  }
  
  public static final RequestParameter IMAGE_ID = ReqParam.ID;
  public static final SqlId FETCH_IMAGE =  new SqlId("FETCH_IMAGE");
  
  @Override  public ResponsePage execute() throws AppException {
    fLogger.fine("Serving image as a binary stream.");
    Id id = getIdParam(ReqParam.ID);
    Image image = Db.fetch(Image.class, FETCH_IMAGE, id);
    HttpServletResponse response = getRequestParser().getResponse();
    //one might set this instead in web.xml, using mime-mapping.
    response.setContentType(image.getFileContentType().getRawString());
    response.setContentLength(image.getFileSize().intValue());
    try {
      //you need to use call response.getWriter() (text) OR response.getOutputStream() (binary)
      InputStream in = image.getInputStream();
      OutputStream out = response.getOutputStream();
      try {
        //send the bytes
        byte[] bucket = new byte[32*1024];
        int bytesRead = 0;
        while(bytesRead != -1){
          bytesRead = in.read(bucket); //-1, 0, or more
          if(bytesRead > 0){
            out.write(bucket, 0, bytesRead);
          }
        }        
      }
      finally {
        //close streams, clean up resources
        if (out != null) out.close();
        if (in != null) in.close();
      }
    }
    catch (IOException ex) {
      throw new AppException("Cannot generate binary output.", ex);
    }
    return getResponsePage();
  }

  // PRIVATE
  
  /** Allows this class to bypass using a JSP. */
  private static final ResponsePage SERVE_IMAGE = ResponsePage.withBinaryData();
  
  private static final Logger fLogger = Util.getLogger(ImageDownloadAction.class);
  
}
