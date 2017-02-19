package hirondelle.web4j.config;

import java.util.*;
import java.text.*;
import java.util.regex.*;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.request.DateConverter;

/** 
  Implementation of {@link DateConverter}, required by WEB4J.
  
  <P>Dates are formatted in the style, using Jan 31, 2006 at 01:59 as an example:
   <ul>     <li>eye-friendly format:  '01/31/2006 01:59'; any trailing '00:00' or ':' is removed.     <li>hand-friendly format: 01312006 01:59; if time portion is absent, 00:00 is assumed.   </ul>
*/
public final class DateConverterImpl implements DateConverter { 
  
  public String formatEyeFriendlyDateTime(DateTime aDateTime, Locale aLocale){
    return chopOffColon(chopOffMidnight(aDateTime.format("MM/DD/YYYY hh:mm")));
  }
  
  public DateTime parseEyeFriendlyDateTime(String aInputValue, Locale aLocale){
    return parseDateTime(aInputValue, EYE_FRIENDLY_REGEX);
  }
  
  public DateTime parseHandFriendlyDateTime(String aInputValue, Locale aLocale){
    return parseDateTime(aInputValue, HAND_FRIENDLY_REGEX);
  }
  
  public String formatEyeFriendly(Date aDate, Locale aLocale, TimeZone aTimeZone) {
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm"); //HH 00-23
    format.setTimeZone(aTimeZone);
    String result = format.format(aDate);
    return chopOffMidnight(result);
  }
  
  public Date parseHandFriendly(String aInputValue, Locale aLocale, TimeZone aTimeZone) {
    //if no time portion, then assume 00:00
    return parseDate(aInputValue, HAND_FRIENDLY_REGEX, aTimeZone);
  }
  
  public Date parseEyeFriendly(String aInputValue, Locale aLocale, TimeZone aTimeZone) {
    return parseDate(aInputValue, EYE_FRIENDLY_REGEX, aTimeZone);
  }
  
  // PRIVATE
  
  /*
   Patterns are thread-safe. Matchers and SimpleDateFormats are NOT thread-safe. 
   Items that are not thread-safe can be used only as local variables, not as fields.
  */

  /** Month in the Gregorian calendar: <tt>01..12</tt>.   */
  private static final String MONTH =
    "(01|02|03|04|05|06|07|08|09|10|11|12)"
  ;

  /** Day of the month in the Gregorian calendar: <tt>01..31</tt>.   */
  private static final String DAY_OF_MONTH = 
    "(01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)"
  ;
   
  /** Hours in the day <tt>00..23</tt>.  */
  private static final String HOURS = 
    "(00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23)"
  ;
   
  /** Minutes in an hour <tt>00..59</tt>.  */
  private static final String MINUTES = 
    "((0|1|2|3|4|5)\\d)"  
  ;

  /** Format : 01312006 01:59   */
  private static final Pattern HAND_FRIENDLY_REGEX = 
    Pattern.compile(MONTH + DAY_OF_MONTH + "(\\d\\d\\d\\d)" + "(?: " + HOURS + ":" + MINUTES +  ")?")
  ;
  
  /** Format : 01/31/2006 01:59   */
  private static final Pattern EYE_FRIENDLY_REGEX = 
    Pattern.compile(MONTH + "/" + DAY_OF_MONTH + "/" + "(\\d\\d\\d\\d)" + "(?: " + HOURS + ":" + MINUTES +  ")?")
  ;
  
  /** Simple 'struct' to hold related items. */
  private static final class DateTimeParts {
    String year;
    String month;
    String day;
    String hour;
    String minute;
  }
  
  /**
   Requires the month, day, year to be the first, second, and third groups, respectively. 
   Optionally, hours and minutes can appear as 4th and 5th groups, respectively.
  */
  private Date parseDate(String aInputValue, Pattern aRegex, TimeZone aTimeZone){
    Date result = null;
    Matcher matcher = aRegex.matcher(aInputValue);
    if( matcher.matches() ) {
      DateTimeParts parts = getParts(matcher);
      Integer year = new Integer(parts.year);
      Integer month = new Integer(parts.month);
      Integer day = new Integer(parts.day);
      String hour = parts.hour;
      String minute = parts.minute;
      Calendar cal = null;
      if( hour == null ){
        cal = new GregorianCalendar(year.intValue(), month.intValue() - 1, day.intValue(), 0,0,0);
      }
      else {
        Integer hourVal = new Integer(hour);
        Integer minuteVal = new Integer(minute);
        cal = new GregorianCalendar(year.intValue(), month.intValue() - 1, day.intValue(), hourVal.intValue(), minuteVal.intValue(), 0);
      }
      cal.setTimeZone(aTimeZone);
      result = cal.getTime();
    }
    return result;
  }
  
  private DateTime parseDateTime(String aInputValue, Pattern aRegex){
    DateTime result = null;
    Matcher matcher = aRegex.matcher(aInputValue);
    if( matcher.matches() ) {
      DateTimeParts parts = getParts(matcher);
      Integer year = new Integer(parts.year);
      Integer month = new Integer(parts.month);
      Integer day = new Integer(parts.day);
      String hour = parts.hour;
      String minute = parts.minute;
      if( hour == null ){
        result = DateTime.forDateOnly(year, month, day);
      }
      else {
        Integer hourVal = new Integer(hour);
        Integer minuteVal = new Integer(minute);
        result = new DateTime(year, month, day, hourVal, minuteVal, null, null);
      }
    }
    return result;
  }
  
  private DateTimeParts getParts(Matcher aMatcher){
    DateTimeParts result = new DateTimeParts();
    result.month = aMatcher.group(1);
    result.day = aMatcher.group(2);
    result.year = aMatcher.group(3);
    result.hour = aMatcher.group(4);
    result.minute = aMatcher.group(5);
    return result;
  }
  
  private String chopOffMidnight(String aString){
    return chopOffUnwanted(aString, "00:00");
  }
  
  private String chopOffColon(String aString){
    return chopOffUnwanted(aString, ":");
  }
  
  private String chopOffUnwanted(String aString, String aUnwanted){
    String result = aString;
    if ( aString.endsWith(aUnwanted) ) {
      int end = aString.length() - aUnwanted.length() - 1;
      result = result.substring(0,end);
    }
    return result;
  }
}
