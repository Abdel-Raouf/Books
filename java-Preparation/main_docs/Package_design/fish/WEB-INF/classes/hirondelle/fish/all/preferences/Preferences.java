package hirondelle.fish.all.preferences;

import java.util.*;
import java.util.regex.*;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.security.SafeText;
import static hirondelle.web4j.util.Consts.FAILS;

/**
 User preferences. 
 
 <P>Each application will have a different set of user preferences. This application has only 
 one user preference - the {@link Locale} - but many applications will have more than one user 
 preference.
 
 <P>User preferences are placed into session scope by the {@link Login}.
*/
public final class Preferences {

  /** 
   Constructor. 
  
   @param aUserName required, <tt>6..50</tt> characters, no spaces.
   @param aLocale required.
  */
  public Preferences(SafeText aUserName, Locale aLocale) throws ModelCtorException {
    fUserName = aUserName;
    fLocale = aLocale;
    validateState();
  }
  
  /** Return the user name passed to the constructor. */
  public SafeText getUserName() {  return fUserName; }
  
  /** Return the locale passed to the constructor. */
  public Locale getLocale() {  return fLocale;  }
  
  /** Intended for debugging only.  */
  public @Override String toString() {
    return ModelUtil.toStringFor(this);
  }

  public @Override boolean equals( Object aThat ) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      Preferences that = (Preferences) aThat;
      result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
    }
    return result;    
  }

  public @Override int hashCode() {
    if ( fHashCode == 0 ) {
      fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
    }
    return fHashCode;
  }
   
  //PRIVATE//
  private final SafeText fUserName;
  private final Locale fLocale;
  private int fHashCode;
  
  private static final Pattern ACCEPTED_PATTERN = Pattern.compile("(?:\\S){6,50}");

  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    if ( FAILS == Check.required(fUserName,  Check.pattern(ACCEPTED_PATTERN)) ) {
      ex.add("Name is required, 6..50 chars, no spaces.");
    }
    if ( FAILS == Check.required(fLocale) ) {
      ex.add("Language is required.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
   
  private Object[] getSignificantFields(){
    return new Object[] {fUserName, fLocale};
  }
}
