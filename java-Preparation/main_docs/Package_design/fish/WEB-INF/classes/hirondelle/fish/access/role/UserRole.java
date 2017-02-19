package hirondelle.fish.access.role;

import java.util.*;
import java.util.regex.Pattern;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Validator;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;
import static hirondelle.web4j.util.Consts.FAILS;

/** The security roles attached to a user. */
public final class UserRole {  

  /**
   Full constructor.
   
   <P>(Note that this Model Object is somewhat unusual since it has a 
   {@link List} as a constructor argument.) 
   
   @param aUserName user name (required), 6..50 characters, no spaces.
   @param aRoles required, possibly empty  
  */
  public UserRole (SafeText aUserName, List<Id> aRoles) throws ModelCtorException {
    fUserName = aUserName;
    fRoles = Collections.unmodifiableList(aRoles);
    validateState();
  }
  
  /** Return the user name passed to the constructor.  */
  public SafeText getUserName(){  return fUserName; }
  
  /** 
   Return an unmodifiable version of the roles passed to the constructor.
   
   <P>Returns an ummodifiable <tt>List</tt>.
  */
  public List<Id> getRoles(){ return fRoles; }
  
  /** Intended for debugging only.  */
  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals( Object aThat ) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      UserRole that = (UserRole) aThat;
      result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
    }
    return result;    
  }

  @Override public int hashCode() {
    if ( fHashCode == 0 ) {
      fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
    }
    return fHashCode;
  }
  
  // PRIVATE //
  private final SafeText fUserName;
  private final List<Id> fRoles;
  private int fHashCode;
  private static final Pattern ACCEPTED_PATTERN = Pattern.compile("(?:\\S){6,50}"); 

  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    Validator validPattern = Check.pattern(ACCEPTED_PATTERN);
    if ( FAILS == Check.required(fUserName, validPattern) ) {
      ex.add("User Name is required, 6..50 chars, no spaces.");
    }
    if ( FAILS == Check.required(fRoles) )  {
      ex.add("Roles is required.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fUserName, fRoles };
  }
}
