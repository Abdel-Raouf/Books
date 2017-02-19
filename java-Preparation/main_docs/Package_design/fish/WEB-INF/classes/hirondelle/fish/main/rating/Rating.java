package hirondelle.fish.main.rating;

import java.math.BigDecimal;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.model.Validator;
import static hirondelle.web4j.util.Consts.FAILS;

/** 
 Rating of a particular {@link hirondelle.fish.main.resto.Resto}.

 <P>Ratings are a kind of "addendum" to {@link hirondelle.fish.main.resto.Resto}. When a <tt>Resto</tt>
 is created, its various ratings are created with initial value <tt>0</tt>. 
 The user may then later edit the Ratings and change them to more appropriate values. 
*/
public final class Rating {

  /**
   Constructor.
   
   <P>The overall rating is a calculated field. In this implementation, the calculation is actually done 
   not in this class, but rather in SQL. This causes some minor repetition, but allows a simple means to 
   alter the calculation, if needed, without needing a redeploy.
    
   @param aId restaurant id  (required), 1..50 characters
   @param aName name of the restaurant (optional), 2..50 characters
   @param aFishRating (required) <tt>0..10</tt> 
   @param aChipsRating (required) <tt>0..10</tt> 
   @param aPriceRating (required) <tt>0..10</tt> 
   @param aLocationRating (required) <tt>0..10</tt> 
   @param aServiceRating (required) <tt>0..10</tt> 
   @param aBeerRating (required) <tt>0..10</tt> 
   @param aOverallRating (optional) <tt>0.00..10.00</tt>
  */
  public Rating (
    Id aId, SafeText aName, Integer aFishRating, Integer aChipsRating, 
    Integer aPriceRating, Integer aLocationRating, Integer aServiceRating, 
    Integer aBeerRating, BigDecimal aOverallRating
  ) throws ModelCtorException {
    fId = aId;
    fName = aName;
    fFishRating = aFishRating;
    fChipsRating = aChipsRating;
    fPriceRating = aPriceRating;
    fLocationRating =aLocationRating;
    fServiceRating = aServiceRating;
    fBeerRating = aBeerRating;
    fOverallRating = aOverallRating;
    validateState();
  }

  /** Return the Id passed to the constructor.   */
  public Id getId(){  return fId; }
  /** Return the name of the restaurant passed to the constructor.   */
  public SafeText getRestaurant(){  return fName; }
  /** Return the fish rating passed to the constructor.   */
  public Integer getFishRating(){  return fFishRating; }
  /** Return the chips rating passed to the constructor.   */
  public Integer getChipsRating(){  return fChipsRating; }
  /** Return the price rating passed to the constructor.   */
  public Integer getPriceRating(){  return fPriceRating; }
  /** Return the location rating passed to the constructor.   */
  public Integer getLocationRating(){  return fLocationRating; }
  /** Return the service rating passed to the constructor.   */
  public Integer getServiceRating(){  return fServiceRating; }
  /** Return the beer rating passed to the constructor.   */
  public Integer getBeerRating(){  return fBeerRating; }
  /** Return the overall rating passed to the constructor.   */
  public BigDecimal getOverallRating(){ return fOverallRating; }
  
  /** Intended for debugging only.   */
  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals( Object aThat ) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      Rating that = (Rating) aThat;
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
  private final SafeText fName;
  private final Integer fFishRating;
  private final Integer fChipsRating;
  private final Integer fPriceRating;
  private final Integer fLocationRating;
  private final Integer fServiceRating;
  private final Integer fBeerRating;
  private final BigDecimal fOverallRating;
  private int fHashCode;

  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    Validator[] ratingsChecks = {Check.min(0), Check.max(10)};
    if ( FAILS == Check.required(fId, Check.range(1,50)) ) {
      ex.add("Id is required, 1..50 chars.");
    }
    if ( FAILS ==  Check.optional(fName, Check.range(2,50)) ) {
      ex.add("Name is optional, 2..50 chars.");
    }
    if ( FAILS == Check.required(fFishRating, ratingsChecks) ) {
      ex.add("Fish Rating is required, 0..10.");
    }
    if ( FAILS == Check.required(fChipsRating, ratingsChecks) ) {
      ex.add("Chips Rating is required, 0..10.");
    }
    if ( FAILS == Check.required(fPriceRating, ratingsChecks) ) {
      ex.add("Price Rating is required, 0..10.");
    }
    if ( FAILS == Check.required(fLocationRating, ratingsChecks) ) {
      ex.add("Location Rating is required, 0..10.");
    }
    if ( FAILS == Check.required(fServiceRating, ratingsChecks) ) {
      ex.add("Service Rating is required, 0..10.");
    }
    if ( FAILS == Check.required(fBeerRating, ratingsChecks) ) {
      ex.add("Beer Rating is required, 0..10.");
    }
    BigDecimal ZERO = new BigDecimal("0.00");
    BigDecimal TEN =  new BigDecimal("10.00");
    if ( FAILS == Check.optional(fOverallRating, Check.range(ZERO, TEN))  ) {
      ex.add("Overall Rating is optional, 0.00..10.00.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {
      fName, fFishRating,fChipsRating, fPriceRating, 
      fLocationRating, fServiceRating, fBeerRating
    };
  }
}
