package hirondelle.fish.help;

import javax.servlet.jsp.JspException;
import java.util.logging.*;
import javax.servlet.jsp.PageContext;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.util.Consts;
import hirondelle.web4j.ui.tag.TagHelper;

/**
 Conditionally display help text in a page.
 
 <P>Example use case :<br>
 <PRE>
 &lt;w:help&gt;
  Report on wildebeest population density.
  Sorted by country, shows only data from 1952-1995.
 &lt;/w:help&gt;
 </PRE>
 
 <P>This tag searches session scope for an object identified by a certain {@link #KEY}.
 If that object is present, and corresponds to {@link Boolean#TRUE}, then the 
 body of this text will be emitted. Otherwise, the body of this tag will not be 
 emitted.
 
 <P>The toggling of this value is performed by {@link ShowHelpAction}.
*/
public final class ShowHelpTag extends TagHelper {

  /** 
   Key which identifies a {@link Boolean} attribute placed in session scope.
   The value of this attribute toggles the display of help text on and off. 
  */
  public static final String KEY = "web4j_help";
  
  /** See class comment. */
  protected String getEmittedText(String aOriginalBody) throws JspException {
    String result = Consts.EMPTY_STRING;
    Boolean showHelp = Boolean.FALSE;
    Object object = getPageContext().getAttribute(KEY, PageContext.SESSION_SCOPE);
    if ( object != null){
      try {
        showHelp = (Boolean)object;
      }
      catch (ClassCastException ex){
        fLogger.severe("Was expecting Attribute named " + KEY + " to refer to a Boolean value. Actually refers to " + object);
        throw new JspException(ex);
      }
    }
    if (showHelp.booleanValue()){
      result = aOriginalBody;
    }
    return result;
  }
  
  // PRIVATE //
  private static final Logger fLogger = Util.getLogger(ShowHelpTag.class);
}
