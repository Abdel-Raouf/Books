package hirondelle.fish.test.doubles;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.util.Args;
import hirondelle.web4j.model.ModelUtil;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 Fake implementation of {@link HttpServletResponse}, used 
 only for testing outside of the regular runtime environment.
 
 Internally, a fake {@link ServletOutputStream} is used here, which simply
 places the response data in a simple byte array held in memory, with no 
 other ultimate destination such as a file or another stream. 
 Thus, <tt>flush</tt> and <tt>close</tt> are no-operations, and 
 there is no reason to use buffering with such a stream.
 
 <P>Methods associated with buffering are no-operations.
*/
public final class FakeResponse implements HttpServletResponse {

  /*
   Methods added for testing.
  */
  
  /**
   Return the response as a <tt>String</tt>.
   Method added for testing.
  */
  public String getFinalResponse(String aEncoding){
    return fStream.getString(aEncoding);
  }
  
  /**
   Return the response as a <tt>byte[]</tt>.
   Method added for testing.
  */
  public byte[] getFinalResponseAsBytes(){
    return fStream.getBytes();
  }
  
  /** 
   Return the cookies that have been passed to this object. 
   Method added for testing.
  */
  public List<Cookie> getCookies(){  return fCookies;  }
  
  /** 
   Return the response status code. 
   Method added for testing.
  */
  public int getStatus() { return fStatusCode; }
  
  /** 
   Return all headers associated with the response. 
   Method added for testing.
  */
  public List<Header> getHeaders() { return fHeaders; }

  /*
   ServletResponse methods.
  */
  
  public String getCharacterEncoding() {  return fCharacterEncoding;  }
  public void setCharacterEncoding(String aEncoding) {
    if( ! fIsCommitted && ! fHasCalledWriter ) {
      Args.checkForContent(aEncoding);
      fCharacterEncoding = aEncoding;
    }
  }

  public String getContentType() {  return fContentType; }
  public void setContentType(String aContentType) {
    if( ! fIsCommitted ) {
      Args.checkForContent(aContentType);
      StringTokenizer parser = new StringTokenizer(aContentType, ";");
      String contentType = parser.nextToken();
      String charEncoding = parser.nextToken().trim().substring("charset=".length());
      fContentType = aContentType; //always the whole thing
      if( ! fHasCalledWriter && Util.textHasContent(charEncoding) ) {
        setCharacterEncoding(charEncoding);
      }
    }
  }
  
  public Locale getLocale() { return fLocale;  }
  
  /** Does not set the character encoding.  */
  public void setLocale(Locale aLocale) {
    if( ! fIsCommitted ) {
      fLocale = aLocale; 
    }
  } 
  
  public ServletOutputStream getOutputStream() throws IOException {
    if(fHasCalledWriter) throw new IllegalStateException("Cannot use both Stream and Writer.");
    fHasCalledStream = true;
    fStream = new FakeServletOutputStream(this);
    return fStream;
  }

  public PrintWriter getWriter() throws IOException {
    if(fHasCalledStream) throw new IllegalStateException("Cannot use both Stream and Writer.");
    fHasCalledWriter = true;
    fStream = new FakeServletOutputStream(this);
    fWriter = new PrintWriter(new OutputStreamWriter(fStream, fCharacterEncoding));
    return fWriter;
  }

  /** No-operation.  */
  public void setContentLength(int aLength) { }

  public int getBufferSize() {  return fBufferSize; }
  
  public void setBufferSize(int aBufferSize) { fBufferSize = aBufferSize; }

  /** No-operation.  */
  public void flushBuffer() throws IOException {  }
  
  /** No-operation.  */
  public void resetBuffer() { }

  /** Returns <tt>true</tt> is anything has been written to the in-memory stream. */
  public boolean isCommitted() { return fIsCommitted;  }

  /** No-operation.  */
  public void reset() { }

  /*
   HttpServletResponse methods.
  */
  
  public void addCookie(Cookie aCookie) {
    fCookies.add(aCookie);
  }
  
  public void setStatus(int aStatusCode) {
    fStatusCode = aStatusCode;
  }
  
  /** Not implemented - deprecated. */
  public void setStatus(int aStatusCode, String aStatusMessage) { }
  
  public boolean containsHeader(String aName) {
    boolean result = false;
    Args.checkForContent(aName);
    for(Header header : fHeaders){
      if ( aName.equals(header.getName()) ){
        result = true;
        break;
      }
    }
    return result;
  }
  
  public void addHeader(String aName, String aValue) {
    fHeaders.add(new Header(aName, aValue));
  }
  
  public void setHeader(String aName, String aValue) {
    Args.checkForContent(aName);
    Args.checkForContent(aValue);
    if( containsHeader(aName) ){
      replaceExistingHeader(aName, aValue);
    }
    else {
      addHeader(aName, aValue);
    }
  }

  public void addIntHeader(String aName, int aValue) {
    addHeader(aName, new Integer(aValue).toString());
  }

  public void setIntHeader(String aName, int aValue) {
    setHeader(aName, new Integer(aValue).toString());
  }

  public void addDateHeader(String aName, long aValue) {
    String date = new SimpleDateFormat(PATTERN_RFC1123).format(new Date(aValue));
    addHeader(aName, date);
  }
  
  public void setDateHeader(String aName, long aValue) {
    String date = new SimpleDateFormat(PATTERN_RFC1123).format(new Date(aValue));
    setHeader(aName, date);
  }
  
  /** Returns the argument unchanged. */
  public String encodeURL(String aURL) {  return aURL; }

  /** Returns the argument unchanged. */
  public String encodeRedirectURL(String aURL) {  return aURL; }

  /** Deprecated. Returns <tt>null</tt>. */
  public String encodeUrl(String arg0) {  return null; }

  /** Deprecated. Returns <tt>null</tt>. */
  public String encodeRedirectUrl(String arg0) {  return null; }

  public void sendError(int aStatusCode) throws IOException {
    if(fIsCommitted) throw new IllegalStateException("Cannot set status after response is committed."); 
    fStatusCode = aStatusCode;
  }

  public void sendError(int aStatusCode,  String aMessage) throws IOException {
    if(fIsCommitted) throw new IllegalStateException("Cannot set status after response is committed."); 
    fStatusCode = aStatusCode;
  }

  public void sendRedirect(String aLocation) throws IOException {
    if(fIsCommitted) throw new IllegalStateException("Cannot send redirect after response is committed."); 
  }

  /** Holds simple name-value pairs. */
  public static final class Header{
    Header(String aName, String aValue){
      Args.checkForContent(aName);
      Args.checkForContent(aValue);
      fName = aName;
      fValue = aValue;
    }
    public String getName() { return fName; }
    public String getValue() { return fValue; }
    @Override public String toString() {
      return ModelUtil.toStringFor(this);
    }
    private final String fName;
    private final String fValue;
  }
  
  // PRIVATE //
  private String fCharacterEncoding = "ISO-8859-1";
  private String fContentType;
  private Locale fLocale = Locale.ENGLISH;
  
  private FakeServletOutputStream fStream;
  private PrintWriter fWriter;
  private boolean fHasCalledStream;
  private boolean fHasCalledWriter;
  private boolean fIsCommitted;
  private int fBufferSize;
  
  private List<Cookie> fCookies = new ArrayList<Cookie>();
  private int fStatusCode;
  private List<Header> fHeaders = new ArrayList<Header>();
  private static final String PATTERN_RFC1123 =  "EEE, dd MMM yyyy HH:mm:ss zzz";
  
  /**
   Simply writes bytes into memory. No buffering required. 
   Flush and close are no-operations.
  */
  private static final class FakeServletOutputStream extends ServletOutputStream{
    FakeServletOutputStream(FakeResponse aFakeResponse){
      //(will expand as required)
      fOutput = new ByteArrayOutputStream(1024);
      fFakeResponse = aFakeResponse;
    }
    @Override public void write(int aByte) throws IOException {
      fOutput.write(aByte);
      fFakeResponse.fIsCommitted = true;
    }
    byte[] getBytes(){
      return fOutput.toByteArray();
    }
    String getString(String aEncoding){
      String result = null;
      try {
        result = fOutput.toString(aEncoding);
      }
      catch (UnsupportedEncodingException ex){
        throw new IllegalArgumentException("Unsupported encoding: " + Util.quote(aEncoding));
      }
      return result;
    }
    /** Not a buffer, but rather the actual destination for the data.  */
    private ByteArrayOutputStream fOutput;
    private FakeResponse fFakeResponse;
  }
  
  private void replaceExistingHeader(String aName, String aValue){
    if( ! containsHeader(aName) ) {
      throw new IllegalArgumentException("Cannot replace header, since does not exist.");
    }
    //remove the old
    Iterator<Header> iter = fHeaders.iterator();
    while ( iter.hasNext() ) {
      Header header = iter.next();
      if( header.getName().equals(aName)) {
        iter.remove();
      }
    }
    //add the new
    addHeader(aName, aValue);
  }
}
