package hirondelle.fish.translate.locale;

import java.util.*;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.web4j.security.SafeText;

/**
 Model Object for the {@link Locale}s supported by this application. 
*/
public final class SupportedLocale {
  
  /**
   Constructor.
   
   @param aId underlying database internal identifier (optional), 1..50 characters
   @param aShortForm mnemonic in the format of {@link Locale#toString()}, has content, 2..50 characters
   @param aDescription of the {@link Locale}, has content, 2..50 characters 
  */
  public SupportedLocale(Id aId, SafeText aShortForm, SafeText aDescription) throws ModelCtorException {
    fId = aId;
    fShortForm = aShortForm;
    fDescription = aDescription;
    validateState();
  }

  /** Return the {@link Id} passed to the constructor. */
  public Id getId(){   return fId;  }
  /** Return the <tt>ShortForm</tt> passed to the constructor. */
  public SafeText getShortForm() { return fShortForm; }
  /** Return the <tt>Description</tt> passed to the constructor. */
  public SafeText getDescription() { return fDescription; }
  
  /** Intended for debugging only. */
  @Override public String toString(){
    return ModelUtil.toStringFor(this);
  }
  
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      SupportedLocale that = (SupportedLocale) aThat;
      result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
    }
    return result;    
  }
  
  @Override public int hashCode(){
    if (fHashCode == 0){
      fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
    }
    return fHashCode;
  }

  // PRIVATE //
  private final Id fId;
  private final SafeText fShortForm;
  private final SafeText fDescription;
  private int fHashCode;
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    if ( FAILS == Check.optional(fId, Check.range(1, 50)) ) {
      ex.add("Id is optional, 1..50 chars.");
    }
    if ( FAILS == Check.required(fShortForm, Check.range(2,50)) ) {
      ex.add("Short Form is required, 2..50 chars.");
    }
    if ( FAILS == Check.required(fDescription, Check.range(2,50)) ) {
      ex.add("Description is required, 2..50 chars.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fShortForm, fDescription};
  }
}
