package hirondelle.fish.translate.basetext;

import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.security.SafeText;
import static hirondelle.web4j.util.Consts.FAILS;

/**
 Model Object for translatable items. 
 
 <P><span class='highlight'>Please see  {@link hirondelle.web4j.ui.translate.Translator} for important information 
 regarding the definition of 'base text'.</span> 
*/
public final class BaseText {

  /**
   Constructor.
   
   @param aId underlying database internal identifier (optional), 1..50 characters
   @param aBaseText to be translated (required) - unique, has content, 1..255 characters
   @param aIsCoderKey required 
  */
  public BaseText(Id aId, SafeText aBaseText, Boolean aIsCoderKey) throws ModelCtorException {
    fId = aId;
    fBaseText = aBaseText;
    fIsCoderKey = Util.nullMeansFalse(aIsCoderKey);
    validateState();
  }
  
  /** Return the {@link Id} passed to the constructor. */
  public Id getId(){ return fId; }
  
  /** Return the <tt>BaseText</tt> passed to the constructor. */
  public SafeText getBaseText() { return fBaseText; }
  
  /** Return the <tt>IsCoderKey</tt> passed to the constructor. */
  public Boolean getIsCoderKey() { return fIsCoderKey; }

  /** Intended for debugging only. */
  @Override public String toString(){
    return ModelUtil.toStringFor(this);
  }
  
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      BaseText that = (BaseText) aThat;
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
  private final SafeText fBaseText;
  private final Boolean fIsCoderKey;
  private int fHashCode;
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    if ( FAILS == Check.optional(fId, Check.range(1,50)) ) {
      ex.add("Id is optional, 1..50 chars.");
    }
    if ( FAILS == Check.required(fBaseText, Check.range(1, 255))) {
      ex.add("Base Text is required, 1..255 chars, and must be unique.");
    }
    if ( FAILS == Check.required(fIsCoderKey)) {
      ex.add("Is Coder Key is required.");
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fBaseText, fIsCoderKey};
  }
}
