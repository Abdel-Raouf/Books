package hirondelle.fish.main.member;

import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.model.Code;
import hirondelle.fish.main.codes.CodeTable;
import static hirondelle.web4j.util.Consts.FAILS;

/** 
 Member of the Fish and Chips Club.
*/
public final class Member {

  /**
   Constructor.
   
   @param aId internal database id, 1..50 characters, optional
   @param aMemberName full name of member, 2..50 characters, required
   @param aIsActive <tt>true</tt> if the member is usually a Fish and Chips participant, <tt>false</tt>
   if the member is no longer active; inactive members are no longer included on the RSVP list. Required. 
   Null is coerced to <tt>false</tt>.  
   @param aDisposition required.
  */
  public Member (Id aId, SafeText aMemberName, Boolean aIsActive, Id aDisposition) throws ModelCtorException {
    fId = aId;
    fName = aMemberName;
    fIsActive = Util.nullMeansFalse(aIsActive);
    fDisposition = CodeTable.codeFor(aDisposition, CodeTable.DISPOSITIONS);
    validateState();
  }

  /** Return the member id passed to the constructor.  */
  public Id getId() { return fId; }
  /** Return the <tt>IsActive</tt> indicator passed to the constructor.  */
  public Boolean getIsActive() {  return fIsActive; }
  /** Return the member name passed to the constructor.  */
  public SafeText getName() { return fName; }
  /** Return the member's disposition passed to the constructor.  */
  public Code getDisposition(){ return fDisposition; }
  
  /** Intended for debugging only.  */
  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals( Object aThat ) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      Member that = (Member) aThat;
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
  private final Boolean fIsActive;
  private final Code fDisposition;
  private int fHashCode;
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    
    if ( FAILS == Check.optional(fId, Check.range(1,50)) ) {
      ex.add("Id is optional, 1..50 chars.");
    }
    if ( FAILS == Check.required(fName, Check.range(2,50))) {
      ex.add("Name is required, 2..50 chars.");
    }
    if ( FAILS == Check.required(fIsActive) ) {
      ex.add("Is Active is required. Inactive Members will not appear in the RSVP list.");
    }
    if ( FAILS == Check.required(fDisposition) ) {
      ex.add("Disposition is required.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[]{fName, fIsActive, fDisposition};
  }
}
