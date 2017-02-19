package hirondelle.fish.exercise.fileupload;

import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Validator;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/** Model Object for an Image. */
public final class Image {

  /**
    Full constructor.
    @param aDescription Description of the image, required, length in range 1..100.
    @param aFileName Name of uploaded image file, required, length in range 1..100, ends in .jpeg,
    .jpg, .gif, or .png.
    @param aFileContentType MIME type of the uploaded file, required, length in range 1..100, must be
    image/jpeg, image/gif, or image/png.
    @param aFileSize Size of the uploaded file, required, must be in range 1K to 50K.
   */
  public Image(Id aId, SafeText aDescription, SafeText aFileName, SafeText aFileContentType, Long aFileSize, InputStream aInputStream)
    throws ModelCtorException {
    fId = aId;
    fDescription = aDescription;
    fFileName = aFileName;
    fFileContentType = aFileContentType;
    fFileSize = aFileSize;
    fInputStream = aInputStream;
    validateState();
  }

  public Id getId() {
    return fId;
  }

  public SafeText getDescription() {
    return fDescription;
  }

  public SafeText getFileName() {
    return fFileName;
  }

  public SafeText getFileContentType() {
    return fFileContentType;
  }

  public Long getFileSize() {
    return fFileSize;
  }
  
  /**
   This is not shown in a listing. It serves to exercise the fetch of an InputStream from the database.
  */
  public InputStream getInputStream() {
    return fInputStream;
  }

  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals(Object aThat) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if (result == null) {
      Image that = (Image)aThat;
      result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
    }
    return result;
  }

  @Override public int hashCode() {
    if (fHashCode == 0) {
      fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
    }
    return fHashCode;
  }

  // PRIVATE 
  private final Id fId;
  private final SafeText fDescription;
  private final SafeText fFileName;
  private final SafeText fFileContentType;
  private final Long fFileSize;
  private final InputStream fInputStream;
  private int fHashCode;
  
  private static List<String> CONTENT_TYPES = Arrays.asList("image/gif", "image/jpeg", "image/png");
  private static List<String> EXTENSIONS = Arrays.asList("gif", "jpeg", "png", "jpg");
  private static Validator CHECK_EXTENSION = new ValidateExtension();
  private static Validator CHECK_CONTENT_TYPE = new ValidateContentType();
  private static final Logger fLogger = Util.getLogger(Image.class);

  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();

    if (FAILS == Check.required(fDescription, Check.range(1, 100))) {
      ex.add("Image Description is required.");
    }
    if (FAILS == Check.required(fFileName, Check.range(1, 100), CHECK_EXTENSION)) {
      ex.add("File Name is required. Must end in .jpeg, .jpg, .gif, or .png");
    }
    else {
      if (FAILS == Check.required(fFileContentType, Check.range(1, 100), CHECK_CONTENT_TYPE)) {
        ex.add("File Content-Type is required. Must be image/jpeg, image/gif, or image/png");
      }
      if (FAILS == Check.required(fFileSize, Check.range(1024, 1024 * 50))) {
        ex.add("File size is required, in range 1K to 50K.");
      }
      if (FAILS == Check.required(fInputStream)) {
        //the end user can't fix this, but at least they can report the problem
        //might want to throw an exception instead, since this is more of a programming bug
        ex.add("InputStream is required.");
      }
    }

    if (!ex.isEmpty()) {
      fLogger.fine(ex.getMessage());
      throw ex;
    }
  }

  private Object[] getSignificantFields() {
    //let's leave out the input stream
    return new Object[]{fDescription, fFileName, fFileContentType, fFileSize};
  }

  private static final class ValidateContentType implements Validator {
    public boolean isValid(Object aItem) {
      SafeText item = (SafeText)aItem;
      return CONTENT_TYPES.contains(item.getRawString());
    }
  }

  private static final class ValidateExtension implements Validator {
    public boolean isValid(Object aItem) {
      SafeText item = (SafeText)aItem;
      int lastPeriod = item.getRawString().lastIndexOf(".");
      String fileExtension = item.getRawString().substring(lastPeriod + 1);
      return EXTENSIONS.contains(fileExtension);
    }
  }
}
