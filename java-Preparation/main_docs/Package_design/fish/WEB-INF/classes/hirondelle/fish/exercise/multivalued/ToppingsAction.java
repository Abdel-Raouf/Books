package hirondelle.fish.exercise.multivalued;

import hirondelle.web4j.BuildImpl;
import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.DateConverter;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 Exercise the case of multivalued request parameters.
*/
public final class ToppingsAction extends ActionTemplateShowAndApply {
  
  public ToppingsAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }
  
  public static final RequestParameter Age = RequestParameter.withLengthCheck("Age");
  public static final RequestParameter DesiredSalary = RequestParameter.withLengthCheck("Desired Salary");
  public static final RequestParameter BirthDate = RequestParameter.withLengthCheck("Birth Date");
  public static final RequestParameter PizzaToppings = RequestParameter.withLengthCheck("Pizza Toppings");
  public static final RequestParameter Artists = RequestParameter.withLengthCheck("Artists");
  public static final RequestParameter TempFile = RequestParameter.withLengthCheck("TempFile");

  /**
   Show a form populated with items from hard-coded <tt>Collection</tt>s.
  */
  @Override protected void show() throws AppException {
    Locale locale = BuildImpl.forLocaleSource().get(getRequestParser().getRequest());
    TimeZone timeZone = BuildImpl.forTimeZoneSource().get(getRequestParser().getRequest());
    fLogger.fine("TimeZone " + timeZone + " Locale " + locale);
    ToppingsEtc toppingsEtc = new ToppingsEtc(timeZone, locale);
    
    DateConverter converter = BuildImpl.forDateConverter();
    for(Date date : toppingsEtc.getBirthDate()){
      fLogger.fine("Birth Date: " + converter.formatEyeFriendly(date, locale, timeZone) );
    }
    
    addToRequest(ITEM_FOR_EDIT, toppingsEtc);
  }
  
  /**
   Extract POSTed collections, and hard-code an error.
   
   <P>The purpose of the error is to exercise the population of the form in that circumstance.
  */
  @Override protected void validateUserInput() throws AppException {
    Collection<SafeText> toppings = getRequestParser().toSafeTexts(PizzaToppings);
    Collection<SafeText> artists = getRequestParser().toSafeTexts(Artists);
    Collection<Integer> ages = getRequestParser().toSupportedObjects(Age, Integer.class);
    Collection<BigDecimal> salaries = getRequestParser().toSupportedObjects(DesiredSalary, BigDecimal.class);
    Collection<Date> birthDates = getRequestParser().toSupportedObjects(BirthDate, Date.class);
    
    fLogger.fine("Toppings Collection : " + toppings);
    fLogger.fine("Artists Collection : " + artists);
    fLogger.fine("Ages Collection : " + ages);
    fLogger.fine("Salaries Collection : " + salaries);
    fLogger.fine("BirthDates Collection : " + birthDates);
    
    addError("This action is hard-coded to fail, to exercise form population.");
  }
  
  /** Never exercised, in this special case, because of the hard-coded error.  */
  @Override  protected void apply() throws AppException {
    //empty
  }
  
  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage("Toppings Etc", "view.jsp", ToppingsAction.class); 
  private static final ResponsePage REDIRECT = new ResponsePage("ToppingsAction.show");
  private static final Logger fLogger = Util.getLogger(ToppingsAction.class);
}
