package hirondelle.web4j.config;

import java.util.*;
import java.util.regex.*;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.request.DateConverter;

/**
 Implementation of {@link DateConverter}, required by WEB4J.
 This application uses dates only, with no time portion. The underlying database column is
 a MySQL Date column, which also handles dates with no time portion.
  
 <P>When your application needs to 
 handle date-plus-time, that case can be handled as well. (The {@link DateTime} class handles
 both cases.)
 
 <P>This application uses only {@link DateTime}, and not {@link Date}. 
 Thus, the methods referring to {@link Date} return null, and are not implemented.
 Other WEB4J example applications do use {@link Date}, however: for example, the <i>Fish and Chips Club</i>. 
*/
public final class DateConverterImpl implements DateConverter {
  
  /**
   Format a {@link DateTime} for the human eye.
   <P>Example return value, for January 31, 2006:<br> 
   <tt>'2009-01-31'</tt>.
  */
  public String formatEyeFriendlyDateTime(DateTime aDateTime, Locale aLocale){
    return aDateTime.format("YYYY-MM-DD");
  }
  
  /**
   Parse a {@link DateTime} entered in an eye-friendly style.
   <P>Example of the required input format, for January 31, 2006 : <br>
   <tt>'2006-01-31'</tt>
  */
  public DateTime parseEyeFriendlyDateTime(String aInputValue, Locale aLocale){
    return parseDateTime(aInputValue, EYE_FRIENDLY_REGEX);
  }
  
  /**
   Parse a {@link DateTime} entered in a hand-friendly style.
   <P>Example of the required input format, for January 31, 2006 : <br>
   <tt>'20060131'</tt>.   
  */
  public DateTime parseHandFriendlyDateTime(String aInputValue, Locale aLocale){
    return parseDateTime(aInputValue, HAND_FRIENDLY_REGEX);
  }
  
  /** Not used, returns null. */
  public String formatEyeFriendly(Date aDate, Locale aLocale, TimeZone aTimeZone) {
    return null;
  }

  /** Not used, returns null. */
  public Date parseEyeFriendly(String aInputValue, Locale aLocale, TimeZone aTimeZone) {
    return null;
  }
  
  /** Not used, returns null. */
  public Date parseHandFriendly(String aInputValue, Locale aLocale, TimeZone aTimeZone) {
    return null;
  }
  
  // PRIVATE
  
  /** Month in the Gregorian calendar: <tt>01..12</tt>.   */
  private static final String MONTH = "(01|02|03|04|05|06|07|08|09|10|11|12)";

  /** Day of the month in the Gregorian calendar: <tt>01..31</tt>.   */
  private static final String DAY_OF_MONTH = 
    "(01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)"
  ;
  
  /** Year in the Gregorian calendar; 4 digits, for example '2010'. */
  private static final String YEAR = "(\\d\\d\\d\\d)";
   
  /** Format : 01312006 */
  private static final Pattern HAND_FRIENDLY_REGEX = Pattern.compile(YEAR + MONTH + DAY_OF_MONTH);
  
  /** Format : 01/31/2006  */
  private static final Pattern EYE_FRIENDLY_REGEX = Pattern.compile(YEAR + "-" + MONTH + "-" + DAY_OF_MONTH);
  
  /** Simple 'struct' to hold related items. */
  private static final class DateTimeParts {
    String year;
    String month;
    String day;
  }

  private DateTime parseDateTime(String aInputValue, Pattern aRegex){
    DateTime result = null;
    Matcher matcher = aRegex.matcher(aInputValue);
    if( matcher.matches() ) {
      DateTimeParts parts = getParts(matcher);
      Integer year = new Integer(parts.year);
      Integer month = new Integer(parts.month);
      Integer day = new Integer(parts.day);
      result = DateTime.forDateOnly(year, month, day);
    }
    return result;
  }
  
  /** Requires the year, month, day, to be the first, second, and third groups, respectively. */
  private DateTimeParts getParts(Matcher aMatcher){
    DateTimeParts result = new DateTimeParts();
    result.year = aMatcher.group(1);
    result.month = aMatcher.group(2);
    result.day = aMatcher.group(3);
    return result;
  }
}