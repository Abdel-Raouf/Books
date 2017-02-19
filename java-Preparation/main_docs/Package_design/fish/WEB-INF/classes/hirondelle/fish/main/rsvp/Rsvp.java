package hirondelle.fish.main.rsvp;

import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;
import static hirondelle.web4j.util.Consts.FAILS;

/** 
 Model Object for a yes/no RSVP response.

 <P>An RSVP starts out in the "unknown" state. Once it has been assigned a 
 yes/no value, however, it cannot move back to the "unknown" state.
 
 <P>All <tt>getXXX</tt> methods simply return the corresponding item passed to 
 the constructor.
*/
public final class Rsvp {

  /**
   Constructor.
    
   @param aVisitId required, 1..50 characters
   @param aMemberId required, 1..50 characters
   @param aMemberName optional, 2..50 characters
   @param aResponse optional
  */
  public Rsvp(Id aVisitId, Id aMemberId, SafeText aMemberName, Boolean aResponse) throws ModelCtorException {
    fVisitId = aVisitId;
    fMemberId = aMemberId;
    fMemberName = aMemberName;
    fResponse = aResponse;
    validateState();
  }
  
  public Id getMemberId() { return fMemberId;  }
  public SafeText getMemberName() { return fMemberName;  }
  public Boolean getResponse() { return fResponse; }
  public void setResponse(Boolean aResponse) { fResponse = aResponse;  }
  public Id getVisitId() { return fVisitId; }

  /** Intended for debugging only.  */
  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals( Object aThat ) {
     Boolean result = ModelUtil.quickEquals(this, aThat);
     if ( result == null ){
       Rsvp that = (Rsvp) aThat;
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
  private final Id fVisitId;
  private final Id fMemberId;
  private final SafeText fMemberName;
  private Boolean fResponse;
  private int fHashCode;
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    if ( FAILS ==  Check.required(fVisitId, Check.range(1, 50)) ) {
      ex.add("Visit Id is required, 1..50 chars.");
    }
    if ( FAILS ==  Check.required(fMemberId, Check.range(1, 50)) ) {
      ex.add("Member Id is required, 1..50 chars.");
    }
    if ( FAILS == Check.optional(fMemberName, Check.range(2, 50)) ) {
      ex.add("Member Name is optional, 2..50 chars.");
    }
    //no check on response - can be null, or true/false
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fMemberName, fResponse};
  }
}
