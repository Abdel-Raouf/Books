package hirondelle.fish.main.resto;

import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.model.Decimal;
import static hirondelle.web4j.model.Decimal.ZERO;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Validator;
import static hirondelle.web4j.util.Consts.FAILS;

/** Model Object for a Restaurant. */
public final class Resto {

  /**
   Full constructor.
    
   @param aId underlying database internal identifier (optional) 1..50 characters
   @param aName of the restaurant (required), 2..50 characters
   @param aLocation street address of the restaurant (optional), 2..50 characters
   @param aPrice of the fish and chips meal (optional) $0.00..$100.00
   @param aComment on the restaurant in general (optional) 2..50 characters
  */
  public Resto(
    Id aId, SafeText aName, SafeText aLocation, Decimal aPrice, SafeText aComment
  ) throws ModelCtorException {
    fId = aId;
    fName = aName;
    fLocation = aLocation;
    fPrice = aPrice;
    fComment = aComment;
    validateState();
  }
  
  public Id getId() { return fId; }
  public SafeText getName() {  return fName; }
  public SafeText getLocation() {  return fLocation;  }
  public Decimal getPrice() { return fPrice; }
  public SafeText getComment() {  return fComment; }

  @Override public String toString(){
    return ModelUtil.toStringFor(this);
  }
  
  @Override public  boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if (result ==  null) {
      Resto that = (Resto) aThat;
      result = ModelUtil.equalsFor(
        this.getSignificantFields(), that.getSignificantFields()
      );
    }
    return result;
  }
  
  @Override public int hashCode(){
    if (fHashCode == 0){
      fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
    }
    return fHashCode;
  }
  
  // PRIVATE 
  private final Id fId;
  private final SafeText fName;
  private final SafeText fLocation;
  private final Decimal fPrice;
  private final SafeText fComment;
  private int fHashCode;
  
  private static final Decimal HUNDRED = Decimal.from("100");

  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    if (FAILS == Check.optional(fId, Check.range(1,50))) {
      ex.add("Id is optional, 1..50 chars.");
    }
    if (FAILS == Check.required(fName, Check.range(2,50))) {
      ex.add("Restaurant Name is required, 2..50 chars.");
    }
    if (FAILS == Check.optional(fLocation, Check.range(2,50))) {
      ex.add("Location is optional, 2..50 chars.");
    }
    Validator[] priceChecks = {Check.range(ZERO, HUNDRED), Check.numDecimalsAlways(2)};
    if (FAILS == Check.optional(fPrice, priceChecks)) {
      ex.add("Price is optional, 0.00 to 100.00.");
    }
    if (FAILS == Check.optional(fComment, Check.range(2,50))) {
      ex.add("Comment is optional, 2..50 chars.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fName, fLocation, fPrice, fComment};
  }
}
