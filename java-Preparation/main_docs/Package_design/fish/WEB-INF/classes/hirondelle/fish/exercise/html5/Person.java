package hirondelle.fish.exercise.html5;

import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.model.Decimal;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.security.SafeText;

import java.math.BigDecimal;

/**  Simple test object.*/
public final class Person {

  /** Constructor.  */
  public Person (
    Id aId, SafeText aName, SafeText aEmail, SafeText aWebsite, Decimal aWeight, 
    SafeText aPhone, SafeText aColor, Decimal aRating, DateTime aBorn
  ) throws ModelCtorException {
    fId = aId;
    fName = aName;
    fEmail = aEmail;
    fWebsite = aWebsite;
    fWeight = aWeight;
    fPhone = aPhone;
    fColor = aColor;
    fRating = aRating;
    fBorn = aBorn;
    validateState();
  }

  public Id getId() { return fId; }
  public SafeText getName() { return fName; }
  public SafeText getEmail() { return fEmail; }
  public SafeText getWebsite() { return fWebsite; }
  public Decimal getWeight() { return fWeight; }
  public SafeText getPhone() { return fPhone; }
  public SafeText getColor() { return fColor; }
  public Decimal getRating() { return fRating; }
  public DateTime getBorn() { return fBorn; }
  
  /** Intended for debugging only.  */
  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals( Object aThat ) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      Person that = (Person) aThat;
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
  
  // PRIVATE 
  private final Id fId;
  private final SafeText fName;
  private final SafeText fEmail;
  private final SafeText fWebsite;
  private final Decimal fWeight;
  private final SafeText fPhone;
  private final SafeText fColor;
  private final Decimal fRating;
  private final DateTime fBorn;
  private int fHashCode;
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    
    if ( FAILS == Check.optional(fId, Check.range(1,50)) ) {
      ex.add("Id is optional, 1..50 chars.");
    }
    if ( FAILS == Check.required(fName, Check.range(2,50))) {
      ex.add("Name is required, 2..50 chars.");
    }
    //more robust checks on email and urls are possible, but left out here
    if ( FAILS == Check.optional(fEmail, Check.range(1,15))) {
      ex.add("Email is optional, 1..15 chars.");
    }
    if ( FAILS == Check.optional(fWebsite, Check.range(1,50))) {
      ex.add("Website is optional, 1..50 chars.");
    }
    Decimal MIN = new Decimal(new BigDecimal("1"));
    Decimal MAX = new Decimal(new BigDecimal("100"));
    if ( FAILS == Check.optional(fWeight, Check.range(MIN, MAX))) {
      ex.add("Weight is optional, range 1..100.");
    }
    if ( FAILS == Check.optional(fPhone, Check.range(5,10))) {
      ex.add("Phone is optional, 5..10 chars.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[]{fName, fEmail, fWebsite, fWeight, fPhone, fColor, fRating, fBorn};
  }
}
