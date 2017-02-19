package hirondelle.electricity.main.home;

import static hirondelle.web4j.model.Decimal.ZERO;
import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.electricity.codes.CodeTable;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Code;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.model.Decimal;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

public final class Spending {
  
  /**
  * Constructor.
  * 
  * @param aId optional
  * @param aDatePaid required, year in range 2000..2100, must have year-month-day
  * @param aPayment required, in range 0.00..1000.00
  * @param aKiloWattHours required, range 0.1..500.0
  * @param aIsEstimated optional, coerced to false if null 
  * @param aFacility required, code for residential or commercial property  
  * @param aComment optional, length 0..100 characters
  */
  public Spending(Id aId, DateTime aDatePaid, Decimal aPayment, Decimal aKiloWattHours, Boolean aIsEstimated, Id aFacility, SafeText aComment) throws ModelCtorException {
    fId = aId;
    fDatePaid = aDatePaid;
    fPayment = aPayment;
    fKiloWattHours = aKiloWattHours;
    fIsEstimated = Util.nullMeansFalse(aIsEstimated);
    fFacility = CodeTable.codeFor(aFacility, CodeTable.FACILITY);
    fComment = aComment;
    validateState();
  }
  
  public Id getId() { return fId; }
  public DateTime getDatePaid() {  return fDatePaid; }
  public Decimal getPayment() { return fPayment;  }
  public Decimal getKilowattHours() {  return fKiloWattHours; }
  public Boolean getIsEstimated() { return fIsEstimated; }
  public Code getFacility() { return fFacility; }
  public SafeText getComment() { return fComment;  }
  
  /**  * Amount paid per kilowatt-hour.
  * 
  * <P>This is an example of a calculated, derived field.
  */
  public Decimal getPaymentPerKWH(){
    return fPayment.div(fKiloWattHours);
  }
  
  @Override public String toString(){
    return ModelUtil.toStringFor(this);
  }
  
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result ==  null ) {
      Spending that = (Spending) aThat;
      result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
    }
    return result;
  }
  
  @Override public int hashCode(){
    return ModelUtil.hashCodeFor(getSignificantFields());
  }
  
  // PRIVATE 
  private final Id fId;
  private final DateTime fDatePaid;
  private final Decimal fPayment;
  private final Decimal fKiloWattHours;
  private final Boolean fIsEstimated;
  private final Code fFacility;
  private final SafeText fComment;

  /* Constants used in validation:  */
  private static final DateTime YEAR_2000 = DateTime.forDateOnly(2000,1,1); 
  private static final DateTime YEAR_2100 = DateTime.forDateOnly(2100,12,31); 
  private static final Decimal THOUSAND = Decimal.from("1000");
  private static final Decimal MIN_KWH = Decimal.from("0.1"); 
  private static final Decimal MAX_KWH = Decimal.from("500");
  
  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    
    if ( FAILS == Check.required(fDatePaid, Check.range(YEAR_2000, YEAR_2100)) ) {
      ex.add("Date paid in range 2000..2100");
    }
    if(fDatePaid != null && ! fDatePaid.hasYearMonthDay()){
      ex.add("Date paid must include year-month-day");
    }
    if ( FAILS == Check.required(fPayment, Check.range(ZERO, THOUSAND), Check.numDecimalsAlways(2)) ) {
      ex.add("Amount paid in range 0.00..1000.00 (2 decimals)");
    }
    if ( FAILS == Check.required(fKiloWattHours, Check.range(MIN_KWH, MAX_KWH), Check.numDecimalsAlways(1))) {
      ex.add("Kilowatt-Hours in range 0.0..500.0 (1 decimal)");
    }
    if ( FAILS == Check.required(fIsEstimated) ) {
      ex.add("Estimated is required.");
    }
    if ( FAILS == Check.required(fFacility) ) {
      ex.add("Building Type is required.");
    }
    if ( FAILS == Check.optional(fComment, Check.range(0,100))) {
      ex.add("Comment cannot be longer than 1,000 characters.");
    }
    
    if ( ex.isNotEmpty() )  {
      throw ex;
    }
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fDatePaid, fPayment, fKiloWattHours, fIsEstimated, fFacility, fComment};
  }
}
