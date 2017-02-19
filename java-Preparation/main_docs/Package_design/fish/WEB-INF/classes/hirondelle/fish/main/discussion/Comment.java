package hirondelle.fish.main.discussion; 

import java.util.*;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.security.SafeText;
import static hirondelle.web4j.util.Consts.FAILS;

/** 
 Comment posted by a possibly-anonymous user.
*/
public final class Comment { 

  /**
   Constructor. 
     
   @param aUserName identifies the logged in user posting the comment. 
   @param aBody the comment, must have content.
   @param aDate date and time when the message was posted.
  */
  public Comment (
    SafeText aUserName, SafeText aBody, Date aDate
  ) throws ModelCtorException {
    fUserName = aUserName;
    fBody = aBody;
    fDate = aDate.getTime();
    validateState();
  }

  /** Return the logged in user name passed to the constructor. */
  public SafeText getUserName() {
    return fUserName;
  }

  /** Return the body of the message passed to the constructor.  */
  public SafeText getBody() {
    return fBody;
  }

  /**
   Return a <a href="http://www.javapractices.com/Topic15.cjp">defensive copy</a> 
   of the date passed to the constructor.
   
   <P>The caller may change the state of the returned value, without affecting 
   the internals of this <tt>Comment</tt>. Such copying is needed since 
   a {@link Date} is a mutable object.
  */
  public Date getDate() {
    // the returned object is independent of fDate
    return new Date(fDate);
  }

  /** Intended for debugging only. */
  @Override public String toString() {
    return ModelUtil.toStringFor(this);
  }

  @Override public boolean equals( Object aThat ) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      Comment that = (Comment) aThat;
      result = ModelUtil.equalsFor(
        this.getSignificantFields(), that.getSignificantFields()
      );
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
  private final SafeText fBody;
  /** Long is used here instead of Date in order to ensure immutability.*/
  private final long fDate;
  private int fHashCode;
  
  private Object[] getSignificantFields(){
    return new Object[] {fUserName, fBody, new Date(fDate)};
  }
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    if( FAILS ==  Check.required(fUserName) ) {
      ex.add("User name must have content.");
    }
    if ( FAILS == Check.required(fBody) ) {
      ex.add("Comment body must have content.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
}
