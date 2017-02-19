package hirondelle.fish.main.search;

import java.util.regex.Pattern;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.model.ModelUtil;
import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.model.Decimal;
import static hirondelle.web4j.model.Decimal.ZERO;;

/**
 Model Object for a search on a restaurant.
 
 <P>This Model Object is a bit unusual since its data is never persisted, 
 and no such objects are returned by a DAO. It exists for these reasons :
 <ul>
 <li>perform validation on user input
 <li>gather together all criteria into one place
 </ul> 
 
 <P><em>Design Note</em><br>
 This class is different from the usual Model Object.
 Its <tt>getXXX</tt> methods are package-private, since it is used only by {@link RestoSearchAction}, 
 and not in a JSP.
*/
public final class RestoSearchCriteria {
  
  enum SortColumn {Name, Price};

  /**
   Constructor.
  
   <P>At least one criterion must be entered, on either the name, or the price range.
   When a price is specified, both minimum and maximum must be included.
   Some restaurants do not have an associated price. Such records will NOT 
   be retrieved.
   
   @param aStartsWith (optional) first few letters of the restaurant name. Cannot be longer 
   than {@link #MAX_LENGTH} characters. Cannot contain the {@link #WILDCARD} character.
   @param aMinPrice (optional) minumum price for the cost of lunch, in the range <tt>0.00..100.00</tt>. 
   Must be less than or equal to <tt>aMaxPrice</tt>, 2 decimals.
   @param aMaxPrice (optional) minumum price for the cost of lunch, in the range <tt>0.00..100.00</tt>, 2 decimals.
   @param aOrderBy (optional) is converted internally into an element of the {@link SortColumn} enumeration.
   @param aIsReverseOrder (optional) toggles the sort order, <tt>ASC</tt> versus <tt>DESC</tt>.
  */
  public RestoSearchCriteria(
    SafeText aStartsWith, Decimal aMinPrice, Decimal aMaxPrice, 
    SafeText aOrderBy, Boolean aIsReverseOrder
  ) throws ModelCtorException {
    fStartsWith = aStartsWith;
    fMinPrice = aMinPrice;
    fMaxPrice = aMaxPrice;
    fOrderBy = aOrderBy == null ? null : SortColumn.valueOf(aOrderBy.getRawString());
    fIsReverseOrder = Util.nullMeansFalse(aIsReverseOrder);
    validateState();
  }

  /** Value {@value} - SQL wildcard character, not permitted as part of input user name.  */
  static final String WILDCARD = "%";
  /** Value {@value} - maximum length of the input restaurant name. */
  static final int MAX_LENGTH = 20;
  
  /** 
   Return user input for <tt>Starts With</tt>, concatenated with {@link #WILDCARD}.
   
   <P>If the user has not entered any <tt>Starts With</tt> criterion, then return <tt>null</tt>.
  */
  SafeText getStartsWith() {  
    return Util.textHasContent(fStartsWith) ? SafeText.from(fStartsWith + WILDCARD) : null; 
  }
  Decimal getMinPrice() {  return fMinPrice; }
  Decimal getMaxPrice() {  return fMaxPrice; }
  SortColumn getOrderBy() {  return fOrderBy; }
  Boolean isReverseOrder() {  return fIsReverseOrder; }

  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }
  
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      RestoSearchCriteria that = (RestoSearchCriteria) aThat;
      result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
    }
    return result;    
  }
  
  @Override public int hashCode(){
    return ModelUtil.hashCodeFor(getSignificantFields());
  }
  
  // PRIVATE 
  private final SafeText fStartsWith;
  private final Decimal fMinPrice;
  private final Decimal fMaxPrice;
  private final SortColumn fOrderBy;
  private final Boolean fIsReverseOrder;
  
  private static final Decimal HUNDRED = Decimal.from("100.00");
  private static final Pattern NO_WILDCARD = Pattern.compile(".*[^" + WILDCARD + "]$");
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    
    if( FAILS == Check.optional(fStartsWith, Check.max(MAX_LENGTH)) ) {
      ex.add("Please enter a shorter Restaurant Name.");
    }
    if( FAILS ==  Check.optional(fStartsWith, Check.pattern(NO_WILDCARD)) ){
      ex.add("Restaurant name (_1_) cannot have this character at the end : _2_", fStartsWith, Util.quote(WILDCARD));
    }
    if( FAILS == Check.optional(fMinPrice, Check.range(ZERO,HUNDRED), Check.numDecimalsAlways(2)) ){
      ex.add("Minimum Price (_1_) must be in the range 0.00 to 100.00, 2 decimals", fMinPrice.toString());
    }
    if( FAILS == Check.optional(fMaxPrice, Check.range(ZERO,HUNDRED), Check.numDecimalsAlways(2)) ){
      ex.add("Maximum Price (_1_) must be in the range 0.00 to 100.00, 2 decimals", fMaxPrice.toString() );
    }
    if ( fMaxPrice != null || fMinPrice != null ){ 
      if( fMaxPrice == null || fMinPrice == null ){
        ex.add("When specifying price, please specify both minimum and maximum.");
      }
    }
    if( fMaxPrice != null && fMinPrice != null ){
      if ( fMinPrice.gt(fMaxPrice) ){
        ex.add("Minimum price cannot be greater than maximum price.");
      }
    }
    if ( ! Util.textHasContent(fStartsWith) && fMinPrice == null && fMaxPrice == null ) {
      ex.add("Please enter criteria on name and/or price.");
    }
    
    if ( ex.isNotEmpty() ) throw ex; 
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fStartsWith, fMinPrice, fMaxPrice, fOrderBy, fIsReverseOrder};
  }
}
