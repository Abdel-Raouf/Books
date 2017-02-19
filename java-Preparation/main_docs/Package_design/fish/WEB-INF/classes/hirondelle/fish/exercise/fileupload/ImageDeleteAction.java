package hirondelle.fish.exercise.fileupload;

import java.util.logging.Logger;

import hirondelle.fish.main.member.MemberAction;
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

/** Delete a single image, and refresh the image listing. */
public final class ImageDeleteAction extends ActionImpl {

  public ImageDeleteAction(RequestParser aRequestParser){
    super(REDIRECT, aRequestParser);
  }

  public static final RequestParameter IMAGE_ID = ReqParam.ID;
  public static final SqlId DELETE_IMAGE =  new SqlId("DELETE_IMAGE");
  
  @Override public ResponsePage execute() throws AppException {
    fLogger.fine("Deleting image.");
    Id id = getIdParam(ReqParam.ID);
    Db.delete(DELETE_IMAGE, id);
    return getResponsePage();
  }

  // PRIVATE 
  private static final Logger fLogger = Util.getLogger(MemberAction.class);
  private static final ResponsePage REDIRECT =  new ResponsePage("ImageUploadAction.show");
}
