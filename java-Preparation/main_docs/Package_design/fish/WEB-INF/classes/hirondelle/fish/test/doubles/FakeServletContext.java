package hirondelle.fish.test.doubles;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.logging.*;
import hirondelle.web4j.util.Util;

/**
 Fake implementation of {@link ServletContext}, used only for testing outside 
 the regular runtime environment.
 
 <P>This class requires that a System property be set correctly.
 See {@link #ROOT_DIRECTORY} for more information.
*/
public class FakeServletContext implements ServletContext {
  
  /**
   Name of a System property ({@value}) that holds the root directory for this web application 
   (with no trailing separator).
   
   <P>This item exists because <tt>ServletContext</tt> needs to translate 
   request paths into explicit file system paths.
    
   <P><span class='highlight'>When using fake objects for testing, this System property must be set 
   to a non-empty value</span>, for example :
   <PRE> '<tt>rootDirectory=C:\myname\myprojects\blah</tt>'.</PRE>
   
   <P>There are various ways of setting this property :
   <ul>
   <li>calling {@link hirondelle.fish.test.TESTAll#setRootDirectory()}
   <li>calling {@link System#setProperty(java.lang.String, java.lang.String)} explicitly 
   <li>launching java with a <tt>-D</tt> option, as in :
   </ul>
    
   <P>'<tt>java ... -DrootDirectory=C:\myname\myprojects\blah</tt> ...'.</tt>
  */
  public static final String ROOT_DIRECTORY = "rootDirectory";
  
  /** Value - {@value}.*/
  public static final String SERVER_INFO = "Fake ServletContext For Testing/1.0";
  /** Value - {@value}.*/
  public static final String SERVLET_CONTEXT_NAME =  "Fake ServletContext For Testing";
  
  /**
   Return the non-empty value of the System property identified by 
   {@link #ROOT_DIRECTORY}.
  */
  public static String getRootDirectory(){
    String result = System.getProperty(ROOT_DIRECTORY);
    if (! Util.textHasContent(result) ) {
      throw new IllegalStateException(
        "FakeServletContext needs a System property named "  + ROOT_DIRECTORY + 
        " . The value of that property is : " + Util.quote(result)
      );
    }
    return result;
  }
  
  /** Simple test harness. Creates a <tt>FakeServletContext</tt>, and emits some of its data. */
  private static void main(String... aArgs) throws FileNotFoundException {
    logger("Starting.");
    FakeServletContext fake = new FakeServletContext();
    logger("Init param: " + fake.getInitParameter("MailServer"));
    logger("Real path for web.xml: " + fake.getRealPath("/WEB-INF/web.xml"));
    logger("Real path for /WEB-INF/: " + fake.getRealPath("/WEB-INF/"));
    logger("Resources in /WEB-INF/classes/ :"  + Util.logOnePerLine(fake.getResourcePaths("/WEB-INF/classes/")));
    
    Set<String> pathsForClasses = getFilePathsBelow("/WEB-INF/classes", fake);
    logger("Classes : " + Util.logOnePerLine(pathsForClasses));
    logger("Done.");
  }
  
  /** Constructor */
  public FakeServletContext() throws FileNotFoundException {
    fInitParams = buildInitParams();
    File rootDirectory = new File(getRootDirectory());
    fPathToFile = populateFileMapping(getFileListingBelow(rootDirectory));
    //logger("Path to File mapping " + Util.logOnePerLine(fPathToFile));
  }

  public String getInitParameter(String aParamName) {
    return fInitParams.get(aParamName);
  }

  public Enumeration getInitParameterNames() {
    return Collections.enumeration(fInitParams.keySet());
  }

  /**
   <P>Returns paths to items under the root directory. 
   
   <P>Example return value, given path patern of "/catalog/":
   <PRE>
   "/catalog/blah.html" 
   "/catalog/xyz.html" 
   "/catalog/specials/"
   </PRE>
   Note that '/catalog/' itself if not returned.
   
   @param aPathPattern starts with "/", path to which items are matched; usually ends with '/' ,
   to indicate a directory.
  */
  public Set getResourcePaths(String aPathPattern) {
    //logger("Getting all resource paths for : " + Util.quote(aPathPattern));
    if (!aPathPattern.startsWith("/")) { 
      throw new IllegalArgumentException("Path must start with '/' : " + Util.quote(aPathPattern)); 
    }
    Set<String> result = new LinkedHashSet<String>();
    int maxNumSeparatorsAllowed = numSeparatorsIn(aPathPattern) + 1;
    Set<String> allPaths = fPathToFile.keySet();
    for (String path : allPaths) {
      if (path.startsWith(aPathPattern) && ! path.equalsIgnoreCase(aPathPattern)) {
        if (numSeparatorsIn(path) <= maxNumSeparatorsAllowed) {
          result.add(path);
        }
      }
    }
    return result;
  }

  /**
   Return the full path to an underlying file or directory. The returned String uses file
   separators of the underlying operating system.
   
   @param aPath such as '/index.html'; directories end in a separator.
  */
  public String getRealPath(String aPath) {
    String result = null;
    //logger("Getting real path for : " + Util.quote(aPath));
    if (!aPath.startsWith("/")) { throw new IllegalArgumentException("Path must start with '/' : "
    + Util.quote(aPath)); }
    File file = fPathToFile.get(aPath);
    if (file != null) {
      result = file.getPath();
    }
    return result;
  }

  public InputStream getResourceAsStream(String aPath) {
    InputStream result = null;
    File file = fPathToFile.get(aPath);
    if (file != null) {
      try {
        result = new FileInputStream(file);
      }
      catch (FileNotFoundException ex) {
        throw new IllegalArgumentException("Cannot open file resource as stream: " + ex);
      }
    }
    return result;
  }

  public void setAttribute(String aName, Object aObject) {
    fAttributes.put(aName, aObject);
  }

  public Object getAttribute(String aName) {
    return fAttributes.get(aName);
  }

  public void removeAttribute(String aName) {
    fAttributes.put(aName, null);
  }

  public Enumeration getAttributeNames() {
   return Collections.enumeration(fAttributes.keySet());
  }

  /** Returns {@value #SERVER_INFO}. */
  public String getServerInfo() {
    return SERVER_INFO;
  }

  /** Returns {@value #SERVLET_CONTEXT_NAME}. */
  public String getServletContextName() {
    return SERVLET_CONTEXT_NAME;
  }

  public void log(String aMessage) {
    fLogger.fine(aMessage);
  }

  public void log(Exception aException, String aMessage) {
    fLogger.severe(aException.toString() + ": " + aMessage);
  }

  public void log(String aMessage, Throwable aThrowable) {
    fLogger.severe(aThrowable.getMessage() + ": " + aMessage);
  }

  /** Returns <tt>2</tt>. */
  public int getMajorVersion() {
    return 2;
  }

  /** Returns <tt>4</tt>. */
  public int getMinorVersion() {
    return 4;
  }

  /** Not implemented - returns <tt>null</tt>. */
   public URL getResource(String aPath) throws MalformedURLException {
     return null;
   }
  
  /** Not implemented - returns <tt>null</tt>. */
  public ServletContext getContext(String aPath) {
    return null;
  }
  
  /** Not implemented - returns <tt>null</tt>. */
  public String getContextPath() {
    return null;
  }
  
  /** Not implemented - returns <tt>null</tt>. */
  public String getMimeType(String aFileName) {
    return null;
  }

  /** Not implemented - returns <tt>null</tt>. */
  public RequestDispatcher getRequestDispatcher(String aPath) {
    return null;
  }

  /** Not implemented - returns <tt>null</tt>. */
  public RequestDispatcher getNamedDispatcher(String aServletName) {
    return null;
  }

  /** Not implemented - returns <tt>null</tt>. */
  public Servlet getServlet(String aName) throws ServletException {
    return null;
  }

  /** Not implemented - returns <tt>null</tt>. */
  public Enumeration getServlets() {
    return null;
  }

  /** Not implemented - returns <tt>null</tt>. */
  public Enumeration getServletNames() {
    return null;
  }

  // PRIVATE //
  private Map<String, String> fInitParams;
  private Map<String, Object> fAttributes = new LinkedHashMap<String, Object>();
  private Map<String, File> fPathToFile;
  private static final String SYSTEM_SEPARATOR = System.getProperty("file.separator");
  private static final String STANDARD_SEPARATOR = "/";
  private static final Logger fLogger = Util.getLogger(FakeServletContext.class);
  
  private static Set<String> getFilePathsBelow(String aStartDirectory, ServletContext aContext){
    //logger("Paths below: " + aStartDirectory);
    Set<String> result = new LinkedHashSet<String>();
    Set<String> paths = aContext.getResourcePaths(aStartDirectory);
    for ( String path : paths) {
      if ( isDirectory(path) ) {
        //recursive call !!!
        result.addAll(getFilePathsBelow(path, aContext));
      }
      else {
        result.add(path);
      }
    }
    return result;
  }
  
  private static boolean isDirectory(String aFullFilePath){
    return aFullFilePath.endsWith(STANDARD_SEPARATOR); 
  }

  private static void logger(Object aObject) {
    System.out.println(String.valueOf(aObject));
  }
  
  private Map<String, String> buildInitParams() {
    Map<String, String> result = new LinkedHashMap<String, String>();
    result.put("MailServer", "NONE");
    return result;
  }

  private Map<String, File> populateFileMapping(List<File> aAllFiles) {
    Map<String, File> result = new LinkedHashMap<String, File>();
    for (File file : aAllFiles) {
      result.put(pathFor(file), file);
    }
    return result;
  }

  private String pathFor(File aFile) {
    String result = aFile.getPath();
    result = chopOffBaseDirectory(result);
    result = ensureCanonicalSeparators(result);
    result = ensureDirectoriesEndInStandardSeparator(aFile, result);
    return result;
  }

  private String chopOffBaseDirectory(String aFilePath) {
    return aFilePath.substring(getRootDirectory().length());
  }

  private String ensureCanonicalSeparators(String aFilePath) {
    return SYSTEM_SEPARATOR.equals("\\") ? aFilePath.replace("\\", "/") : aFilePath;
  }

  private String ensureDirectoriesEndInStandardSeparator(File aFile, String aFilePath) {
    return aFile.isDirectory() ? aFilePath + STANDARD_SEPARATOR : aFilePath;
  }

  /**
    Recursively walk a directory tree and return a List of all Files found; the List is
    sorted using File.compareTo.
    @param aStartingDir is a valid directory, which can be read.
   */
  private List<File> getFileListingBelow(File aStartingDir) throws FileNotFoundException {
    validateDirectory(aStartingDir);
    List<File> result = new ArrayList<File>();
    File[] filesAndDirs = aStartingDir.listFiles();
    List<File> filesDirs = Arrays.asList(filesAndDirs);
    for (File file : filesDirs) {
      result.add(file); // always add, even if directory
      if (!file.isFile()) {
        // must be a directory
        // recursive call!
        List<File> deeperList = getFileListingBelow(file);
        result.addAll(deeperList);
      }
    }
    Collections.sort(result);
    return result;
  }

  private void validateDirectory(File aDirectory) throws FileNotFoundException {
    if (aDirectory == null) { 
      throw new IllegalArgumentException("Directory should not be null."); 
    }
    if (!aDirectory.exists()) { 
      throw new FileNotFoundException("Directory does not exist: " + aDirectory); 
    }
    if (!aDirectory.isDirectory()) { 
      throw new IllegalArgumentException("Is not a directory: " + aDirectory); 
    }
    if (!aDirectory.canRead()) { 
      throw new IllegalArgumentException("Directory cannot be read: " + aDirectory); 
    }
  }

  /** Count the number of standard separators in aPath. */
  private int numSeparatorsIn(String aPath) {
    char standardSeparator = STANDARD_SEPARATOR.charAt(0);
    int result = 0;
    StringCharacterIterator iterator = new StringCharacterIterator(aPath);
    char character = iterator.current();
    while (character != CharacterIterator.DONE) {
      if (character == standardSeparator) {
        result++;
      }
      character = iterator.next();
    }
    return result;
  }
}
