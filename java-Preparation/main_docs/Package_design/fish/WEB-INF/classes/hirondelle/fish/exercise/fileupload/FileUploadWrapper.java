package hirondelle.fish.exercise.fileupload;

import java.util.*;
import java.io.*;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import static hirondelle.web4j.util.Consts.EMPTY_STRING;
import hirondelle.web4j.util.Util;

/**
 Wrapper for a file upload request.
 
 <P>File upload requests are not handled graciously by the Servlet API. 
 This class is used as a wrapper around the underlying file upload request, to 
 allow it to behave much as a regular request.
 
 <P>This class uses the Apache Commons 
 <a href='http://commons.apache.org/fileupload/'>File Upload tool</a>.
 The generous Apache License will very likely allow you to use it in your 
 applications as well. 
*/
public class FileUploadWrapper extends HttpServletRequestWrapper {
  
  /** Constructor.  */
  public FileUploadWrapper(HttpServletRequest aRequest) throws IOException {
    super(aRequest);
    ServletFileUpload upload = new ServletFileUpload( new DiskFileItemFactory());
    try {
      List<FileItem> fileItems = upload.parseRequest(aRequest);
      convertToMaps(fileItems);
    }
    catch(FileUploadException ex){
      throw new IOException("Cannot parse underlying request: " + ex.toString());
    }
  }
  
  /**
   Return all request parameter names, for both regular controls and file upload 
   controls.
   
   <P>Returning the name of file upload controls allows checking of the returned
   param names versus a 'white list' of expected names. This increases security, 
   which is especially important for file upload forms.
   See {@link hirondelle.web4j.security.ApplicationFirewallImpl} as well.
   
   <P>Does not include query params passed to 
   <tt>request.getRequestDispatcher(String)</tt>.
  */
  @Override public Enumeration getParameterNames() {
    Set<String> allNames = new LinkedHashSet<String>();
    allNames.addAll(fRegularParams.keySet());
    allNames.addAll(fFileParams.keySet());
    return Collections.enumeration(allNames);
  }
  
  /**
   Return the parameter value. Applies only to regular parameters, not to 
   file upload parameters. Includes query params passed to 
   <tt>request.getRequestDispatcher(String)</tt>.
   
   <P>If the parameter is not present in the underlying request, 
   then <tt>null</tt> is returned.
   <P>If the parameter is present, but has no  associated value, 
   then an empty string is returned.
   <P>If the parameter is multivalued, return the first value that 
   appears in the request.  
  */
  @Override public String getParameter(String aName) {
    String result = null;
    List<String> values = fRegularParams.get(aName);
    if( values == null ){
      //try the wrappee, to see if it has a value 
      //(needed for query params passed to RequestDispatcher - Template mechanism.)
      String superValue = super.getParameter(aName);
      if( Util.textHasContent(superValue)) {
        result = superValue;
      }
    }
    else if ( values.isEmpty() ) {
      //param name known, but no values present
      result = EMPTY_STRING;
    }
    else {
      //return first value in list
      result = values.get(FIRST_VALUE);
    }
    return result;
  }
  
  /**
   Return the parameter values. Applies only to regular parameters, 
   not to file upload parameters.
   <P>Does not include query params passed to 
   <tt>request.getRequestDispatcher(String)</tt>.
  */
  @Override public String[] getParameterValues(String aName) {
    String[] result = null;
    List<String> values = fRegularParams.get(aName);
    if( values != null ) {
      result = values.toArray(new String[values.size()]);
    }
    return result;
  }
  
  /**
   Return a {@code Map<String, String[]>} for all regular parameters.
   Does not return any file upload paramters at all. 
   Does not include query params passed to 
   <tt>request.getRequestDispatcher(String)</tt>.
  */
  @Override public Map getParameterMap() {
    Map<String, String[]> result = new LinkedHashMap<String, String[]>();
    for(String key: fRegularParams.keySet()){
       result.put(key, fRegularParams.get(key).toArray(new String[0]));
    }
    return Collections.unmodifiableMap(result);
  }
  
  
  /**
   Return a {@code List<FileItem>}, in the same order as they appear
    in the underlying request.
  */
  public List<FileItem> getFileItems(){
    return new ArrayList<FileItem>(fFileParams.values());
  }
  
  /**
   Return the {@link FileItem} of the given name.
   <P>If the name is unknown, then return <tt>null</tt>.
  */
  public FileItem getFileItem(String aFieldName){
    return fFileParams.get(aFieldName);
  }
  
  
  // PRIVATE //
  
  /*
   Implementation Note.
   This class parses params in its constructor, using the original underlying request.
   However, calls to request.getRequestDispatcher(String) can contain 'extra' query 
   params, not present in the original request. 
   (That method is important since it implements the WEB4J templating mechanism.)
   Such 'extra' query params will not be automatically visible to this wrapper. 
   Thus, unless special measures are taken, the destination JSP (which sees the 
   wrapper, not the wrappee), will not see such extra query params. 
    
   This class must therefore have some interaction with the 'wrappee' in order to 
   grab such extra query params when needed. In the current implementation, 
   that interaction is minimal, and suffices only to allow JSPs to fetch the extra
   params using getParameter(String). Other implementations may want to extend 
   that, and make it more complete. 
  */
  
  /** Store regular params only. May be multivalued (hence the List).  */
  private final Map<String, List<String>> fRegularParams = 
    new LinkedHashMap<String, List<String>>()
  ;
  
  /** Store file params only. */
  private final Map<String, FileItem> fFileParams = new LinkedHashMap<String, FileItem>();
  private static final int FIRST_VALUE = 0;
  
  private void convertToMaps(List<FileItem> aFileItems){
    for(FileItem item: aFileItems) {
      if ( isFileUploadField(item) ) {
        fFileParams.put(item.getFieldName(), item);
      }
      else {
        if( alreadyHasValue(item) ){
          addMultivaluedItem(item);
        }
        else {
          addSingleValueItem(item);
        }
      }
    }
  }
  
  private boolean isFileUploadField(FileItem aFileItem){
    return ! aFileItem.isFormField();
  }
  
  private boolean alreadyHasValue(FileItem aItem){
    return fRegularParams.get(aItem.getFieldName()) != null;
  }
  
  private void addSingleValueItem(FileItem aItem){
    List<String> list = new ArrayList<String>();
    list.add(aItem.getString());
    fRegularParams.put(aItem.getFieldName(), list);
  }
  
  private void addMultivaluedItem(FileItem aItem){
    List<String> values = fRegularParams.get(aItem.getFieldName());
    values.add(aItem.getString());
  }
}
