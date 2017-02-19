package hirondelle.fish.exercise.fileupload;

import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

/**
 Overwrite the image used on the home page.
 
 <P>This is an example of uploading a file, validating it, and saving it to the file system.
 
 <P>See {@link hirondelle.fish.exercise.fileupload.Image} for details regarding validation.
*/
public final class ImageUploadAction extends ActionTemplateShowAndApply {

  /** Constructor */
  public ImageUploadAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }
  
  /** This 'regular' param will have its value checked by <tt>ApplicationFirewallImpl</tt> in the usual way.  */
  public static final RequestParameter DESCRIPTION = RequestParameter.withLengthCheck("Description");
  /** This file upload param will not have its <em>value</em> checked as would a regular param. */
  public static final RequestParameter IMAGE_FILE = RequestParameter.forFileUpload("ImageFile");

  /** Show a blank form.  */
  @Override protected void show() throws AppException {
    addToRequest(ITEMS_FOR_LISTING, listAllImages());    
  }
  
  /**Ensure user input can create an {@link Image} object.  */
  @Override protected void validateUserInput() throws AppException {
    extractFileUploadFields();
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fImage = builder.build(
        Image.class,
        null /*no Id yet when adding */,
        DESCRIPTION, 
        SafeText.from(fFileItem.getName()), 
        SafeText.from(fFileItem.getContentType()), 
        fFileItem.getSize(),
        fFileItem.getInputStream()
      );
    }
    catch(ModelCtorException ex){
      addError(ex);
    }
    catch(IOException ex){
      addError("Unable to access InputStream.");
    }
  }
  
  /** If user input is valid, overwrite the image file used on the home page; save it to the database as well. */
  @Override protected void apply() throws AppException {
    overwriteImageFile(fImage);
    addMessage("New image has been overwritten in the file system.");
    saveImageToDatabase(fImage);
    addMessage("New image has added to the database.");
  }
  
  public static final SqlId ADD_IMAGE = new SqlId("ADD_IMAGE");
  public static final SqlId LIST_IMAGES = new SqlId("LIST_IMAGES");
  
  // PRIVATE 
  private FileItem fFileItem;
  private Image fImage;
  private static final String IMAGE_FILE_SAVE_AS = "/images/FishAndChips.jpg";
  private static final ResponsePage FORWARD = new ResponsePage("Upload", "view.jsp", ImageUploadAction.class);
  private static final ResponsePage REDIRECT =  new ResponsePage("ImageUploadAction.show");
  private static final Logger fLogger = Util.getLogger(ImageUploadAction.class);
  
  private void extractFileUploadFields(){
    HttpServletRequest request = getRequestParser().getRequest();
    SafeText description = getParam(DESCRIPTION);
    
    //note a cast is needed here to get the file-related data
    FileUploadWrapper wrappedRequest = (FileUploadWrapper)request;
    fFileItem = wrappedRequest.getFileItem(IMAGE_FILE.getName());
    fLogger.fine("Extracted Description: " + description);
    fLogger.fine("Extracted File Name: " + fFileItem.getName());
    fLogger.fine("Extracted File Content Type: " + fFileItem.getContentType());
    fLogger.fine("Extracted File Size : " + fFileItem.getSize());
  }

  private void overwriteImageFile(Image aImage) throws AppException {
    String imageFilePath = getExistingSession().getServletContext().getRealPath(IMAGE_FILE_SAVE_AS);
    File file = new File(imageFilePath);
    try {
      fFileItem.write(file);
    }
    catch(Exception ex){
      throw new AppException("Cannot save file.", ex);
    }
  }

  private List<Image> listAllImages() throws DAOException {
    return Db.list(Image.class, LIST_IMAGES);
  }
  
  private void saveImageToDatabase(Image aImage) throws DAOException {
    Db.add(
      ADD_IMAGE, aImage.getDescription(), aImage.getFileName(), 
      aImage.getFileContentType(), aImage.getFileSize(), aImage.getInputStream()
    );
  }
}
