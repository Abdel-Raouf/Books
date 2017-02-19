package hirondelle.web4j.config;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.TimeZone;

import hirondelle.web4j.model.Decimal;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.model.ConvertParamError;
import hirondelle.web4j.security.SafeText;

/**
 Implementation of {@link ConvertParamError}, required by WEB4J.
 
 <P>Defines how the application renders conversion errors detected by the framework.
*/
public final class ConvertParamErrorImpl implements ConvertParamError { 

  /**
   Return a {@link ModelCtorException} having a compound message. 
   
   See {@link hirondelle.web4j.model.AppResponseMessage} for further information on compound messages.
  */
  public ModelCtorException get(Class<?> aTargetClass, String aUserInputValue, RequestParameter aRequestParameter){
    String errorMessagePattern = getPatternAsFixedString(aTargetClass);
    ModelCtorException result = new ModelCtorException();
    if( SafeText.class == aTargetClass) {
      //User input might be long, so it's not included here as a second param.
      //Ideally, the second param should contain the offending character(s).
      //That is not possible here, unfortunately.
      //Mitigated by treating all chars known to EscapeChars.forHTML() as permitted chars.
      Object[] params = { aRequestParameter }; 
      result.add(errorMessagePattern, params);    
    }
    else {
      Object[] params = { aRequestParameter, aUserInputValue };
      result.add(errorMessagePattern, params);    
    }
    return result;
  }

  // PRIVATE 

  /**
   If this were a fully multilingual application, these text items would, in this class, 
   remain as they are. The only difference is that the implementation of 
   {@link hirondelle.web4j.ui.translate.Translator} would need to be aware of these 
   text snippets as 'base text'.
  */
  private String getPatternAsFixedString(Class<?> aTargetClass){
    String result = null;
    if ( BigDecimal.class == aTargetClass || Decimal.class == aTargetClass ) {
      result = "_1_ is not in the expected format/range : '_2_'";
    }
    else if ( Integer.class == aTargetClass ) {
      result = "_1_ is not an integer : '_2_'";
    }
    else if ( hirondelle.web4j.model.DateTime.class == aTargetClass ) {
      result = "_1_ is not in the expected date format 'YYYYMMDD' : '_2_'";
    }
    else if ( SafeText.class == aTargetClass ) {
      result = "_1_ contains unpermitted character(s).";
    }
    else if ( TimeZone.class == aTargetClass ) {
      result = "_1_ is not a valid Time Zone identifier: '_2_'";
    }
    else if ( Locale.class == aTargetClass ) {
      result = "_1_ is not a valid Locale identifier: '_2_'";
    }
    else {
      throw new AssertionError("Unexpected case for target base class: " + aTargetClass);
    }
    return result;
  }
}
