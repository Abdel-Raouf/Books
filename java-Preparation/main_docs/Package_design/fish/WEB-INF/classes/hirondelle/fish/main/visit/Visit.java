package hirondelle.fish.main.visit;

import java.util.*;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.security.SafeText;
import hirondelle.fish.main.codes.CodeTable;
import hirondelle.web4j.model.Code;
import static hirondelle.web4j.util.Consts.FAILS;

/** 
 Model Object for a weekly Visit to a {@link hirondelle.fish.main.resto.Resto}.
 <P>(This class is non-<tt>final</tt> only because of a test case for equals.) 
*/
public /*final*/ class Visit {
  
  /**
   Constructor.
    
   @param aId underlying database internal identifier (optional) 1..50 characters
   @param aRestaurantId required. This item is translated internally to its corresponding {@link Code}.
   @param aLunchDate date for the visit (required), year in range 2006..2100. A defensive copy is made for this item.
   @param aMessage shown to the Members on RSVP page (optional), 2..50 characters
  */
  public Visit(Id aId, Id aRestaurantId, Date aLunchDate, SafeText aMessage) throws ModelCtorException {
    fId = aId;
    fRestaurantCode = CodeTable.codeFor(aRestaurantId, CodeTable.RESTAURANTS);
    //defensive copy for a mutable Date object :
    fLunchDate = aLunchDate != null ? new Date(aLunchDate.getTime()) : null;
    fMessage = aMessage;
    validateState();
  }

  /** Return the <tt>Visit</tt> id passed to the constructor.  */
  public Id getId() {  return fId; }
  /** Return the item in {@link CodeTable#RESTAURANTS} corresponding to the restaurant id passed to the constructor. */
  public Code getRestaurant() { return fRestaurantCode;  }
  /** Return a defensive copy of the lunch date passed to the constructor. */
  public Date getLunchDate() { return new Date(fLunchDate.getTime());  } 
  /** Return the message passed to the constructor. */
  public SafeText getMessage() {  return fMessage; }
  
  /** Intended for debugging only. */
  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals(Object aThat) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      Visit that = (Visit) aThat;
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
  private final Id fId;
  private final Code fRestaurantCode;
  private final Date fLunchDate;
  private final SafeText fMessage;
  private int fHashCode;
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    if ( FAILS ==  Check.optional(fId, Check.range(1, 50)) ) {
      ex.add("Visit Id is optional, 1..50 chars.");
    }
    if ( FAILS == Check.required(fRestaurantCode) ) {
      ex.add("Restaurant is required.");
    }
    if ( FAILS == Check.required(fLunchDate, Check.range(MIN_DATE.getTimeInMillis(), MAX_DATE.getTimeInMillis())) ) {
      ex.add("Lunch Date is required, year in range 2006..2100.");
    }
    if ( FAILS == Check.optional(fMessage, Check.range(2, 50)) ) {
      ex.add("Message is optional, 2..50 characters.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  static {
    initDateRangeLimits();
  }
  private static Calendar MIN_DATE;
  private static Calendar MAX_DATE;
  private static void initDateRangeLimits() {
    MIN_DATE = new GregorianCalendar();
    MIN_DATE.set(2006, Calendar.JANUARY, 1, 0, 0, 0);
    MIN_DATE.set(Calendar.MILLISECOND, 0);
    
    MAX_DATE = new GregorianCalendar();
    MAX_DATE.set(2100, Calendar.DECEMBER, 31, 23, 59, 59);
    MAX_DATE.set(Calendar.MILLISECOND, 999);
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fRestaurantCode, fLunchDate, fMessage};
  }
}
