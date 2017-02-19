package hirondelle.fish.exercise.multivalued;

import java.util.*;
import java.math.BigDecimal;
import hirondelle.web4j.security.SafeText;

/**
 Toy model class, used to quickly test domain items modelled as a 
 <tt>Collection</tt>.

<P>This class helps to test :
<ul>
 <li>populating form elements with a <tt>Collection</tt> 
 <li>translating multiple-valued request parameters into a <tt>Collection</tt>  
</ul>

 The various <tt>getXXX</tt> methods :
 <ul>
 <li>return immutable <tt>Collections</tt>
 <li>return simple, hard-coded toy data. (This allows for rapid creation of test cases.)
</ul>
*/
public final class ToppingsEtc {
  
  public ToppingsEtc(TimeZone aTimeZone, Locale aLocale){
    fTimeZone = aTimeZone;
    fLocale = aLocale;
  }
  

  /**
   Returns hard-coded items - <tt>Anchovies</tt> and <tt>Salami</tt>
  */
  public Collection<SafeText> getPizzaToppings(){
    return Arrays.asList(SafeText.from("Anchovies"), SafeText.from("Salami"));
  }

  /**
   Returns hard-coded items - <tt>Tom Thomson</tt>, <tt>Alex Janvier</tt>, 
   and <tt>Mary Pratt</tt>.
  */
  public Collection<SafeText> getArtists(){
    return Arrays.asList(SafeText.from("Tom Thomson"), SafeText.from("Alex Janvier"), SafeText.from("Mary Pratt"));
  }

  /**
   Returns hard-coded <tt>Integer</tt>s - <tt>10</tt>, <tt>100</tt>, and <tt>20</tt>.
  */
  public Collection<Integer> getAge(){
    return Arrays.asList(new Integer(10), new Integer(100), new Integer(20) );
  }

  /**
   Returns hard-coded <tt>BigDecimal</tt>s - <tt>10,000</tt>, <tt>30,000</tt>, 
   and <tt>100,000</tt>
  */
  public Collection<BigDecimal> getDesiredSalary(){
    return Arrays.asList(
      new BigDecimal("10000.00"), 
      new BigDecimal("30000.00"), 
      new BigDecimal("100000.00")
    );
  }
  
  /**
   Returns hard-coded <tt>java.util.Date</tt>s - <tt>12/31/1969</tt> and 
   <tt>01/01/1970</tt>.
  */
  public Collection<Date> getBirthDate(){
    /*
     All of these sets are needed in order to get the date formatted exactly 
     as expected by the DateConverter. 
    */
    Calendar one = new GregorianCalendar(fTimeZone, fLocale);
    one.set(Calendar.YEAR, 1969);
    one.set(Calendar.MONTH, Calendar.DECEMBER);
    one.set(Calendar.DAY_OF_MONTH, 31);
    one.set(Calendar.HOUR, 0);
    one.set(Calendar.MINUTE, 0);
    one.set(Calendar.SECOND, 0);
    one.set(Calendar.MILLISECOND, 0);
    one.set(Calendar.AM_PM, Calendar.AM);
    
    Calendar two = new GregorianCalendar(fTimeZone, fLocale);
    two.set(Calendar.YEAR, 1970);
    two.set(Calendar.MONTH, Calendar.JANUARY);
    two.set(Calendar.DAY_OF_MONTH, 1);
    two.set(Calendar.HOUR, 0);
    two.set(Calendar.MINUTE, 0);
    two.set(Calendar.SECOND, 0);
    two.set(Calendar.MILLISECOND, 0);
    two.set(Calendar.AM_PM, Calendar.AM);
    
    return Arrays.asList( one.getTime(), two.getTime()  );
  }
  
  private TimeZone fTimeZone;
  private Locale fLocale;
}
