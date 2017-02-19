package hirondelle.web4j.config;

import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.request.DateConverter;
import hirondelle.web4j.util.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

/** JUnit tests for {@link hirondelle.web4j.config.DateConverterImpl}. */
public final class TESTDateConverterImpl extends TestCase  {
  
  /**  Run the test cases.  */
   public static void main(String args[]) {
     String[] testCaseName = { TESTDateConverterImpl.class.getName()};
     junit.textui.TestRunner.main(testCaseName);
  }

   public TESTDateConverterImpl(String aName) {
    super(aName);
  }
   
   public void testFormatDateTime(){
     testFormatDateTime("2009-01-31 15:12", "01/31/2009 15:12");
     testFormatDateTime("2009-01-31 00:00", "01/31/2009");
     testFormatDateTime("2009-01-31", "01/31/2009");
   }
   
   public void testParseEyeDateTime(){
     testParseEyeDateTime("01/31/1962 18:01", "1962-01-31 18:01");
     testParseEyeDateTime("01/31/1962", "1962-01-31");
   }
   
   public void testParseHandDateTime(){
     testParseHandDateTime("01311962 18:01", "1962-01-31 18:01");
     testParseHandDateTime("01312006 01:59", "2006-01-31 01:59");
     testParseHandDateTime("01312006", "2006-01-31");
   }
   
    public void testParseHandSuccess(){
      testParseHand("01011962", new Date(62, 0, 1) );
      testParseHand("01021962", new Date(62, 0, 2) );
      testParseHand("01091962", new Date(62, 0, 9) );
      testParseHand("01101962", new Date(62, 0, 10) );
      testParseHand("01311962", new Date(62, 0, 31) );
      testParseHand("02011962", new Date(62, 1, 1) );
      testParseHand("02281962", new Date(62, 1, 28) );
      testParseHand("03011962", new Date(62, 2, 1) );
      testParseHand("12011962", new Date(62, 11, 1) );
      testParseHand("12311962", new Date(62, 11, 31) );
      testParseHand("12311900", new Date(0, 11, 31) );
      testParseHand("12311800", new Date(-100, 11, 31) );
      testParseHand("12312006", new Date(106, 11, 31) );
      testParseHand("12310100", new Date(-1800, 11, 31) );
      
      testParseHand("12312001", getDate(2001,12,31,0,0) );
      testParseHand("12312001 01:00", getDate(2001,12,31,1,0) );
      testParseHand("12312001 23:59", getDate(2001,12,31,23,59) );
      testParseHand("12312001 01:01", getDate(2001,12,31,1,1) );
      testParseHand("12312001 01:10", getDate(2001,12,31,1,10) );
      testParseHand("12312001 10:00", getDate(2001,12,31,10,0) );
      testParseHand("12312001 10:30", getDate(2001,12,31,10,30) );
      testParseHand("12312001 10:30", getDate(2001,12,31,10,30) );
      
      testParseHand("01311962 18:01", getDate(1962,1,31,18,1) );
      
    }
   
    public void testParseHandFailure(){
      testParseHandFailure("01011962 ");
      testParseHandFailure(" 01011962 ");
      testParseHandFailure(" 01011962");
      testParseHandFailure("A01011962");
      testParseHandFailure("01011962A");
      testParseHandFailure("19620101");
      testParseHandFailure("31011962");
      testParseHandFailure("01X11962");
      testParseHandFailure("010162");
      testParseHandFailure("0101062");
      
      testParseHandFailure("01011962 0");
      testParseHandFailure("01011962 0:00");
      testParseHandFailure("01011962 00:00 ");
      testParseHandFailure("01011962 24:00");
      testParseHandFailure("01011962 00:60");
      testParseHandFailure("01011962 49:49");
    }
    
    public void testParseEyeSuccess(){
      testParseEye("01/01/1962", new Date(62, 0, 1) );
      testParseEye("01/02/1962", new Date(62, 0, 2) );
      testParseEye("01/09/1962", new Date(62, 0, 9) );
      testParseEye("01/10/1962", new Date(62, 0, 10) );
      testParseEye("01/31/1962", new Date(62, 0, 31) );
      testParseEye("02/01/1962", new Date(62, 1, 1) );
      testParseEye("02/28/1962", new Date(62, 1, 28) );
      testParseEye("03/01/1962", new Date(62, 2, 1) );
      testParseEye("12/01/1962", new Date(62, 11, 1) );
      testParseEye("12/31/1962", new Date(62, 11, 31) );
      testParseEye("12/31/1900", new Date(0, 11, 31) );
      testParseEye("12/31/1800", new Date(-100, 11, 31) );
      testParseEye("12/31/2006", new Date(106, 11, 31) );
      testParseEye("12/31/0100", new Date(-1800, 11, 31) );
      
      testParseEye("01/31/1975", getDate(1975,1,31,0,0) );
      testParseEye("01/31/1975 12:00", getDate(1975,1,31,12,0) );
      testParseEye("01/31/1975 01:00", getDate(1975,1,31,1,0) );
      testParseEye("01/31/1975 23:59", getDate(1975,1,31,23,59) );
    }
    
    public void testParseEyeFailure(){
      testParseEyeFailure("01/01/1962 ");
      testParseEyeFailure(" 01/01/1962 ");
      testParseEyeFailure(" 01/01/1962");
      testParseEyeFailure("A01/01/1962");
      testParseEyeFailure("01/01/1962A");
      testParseEyeFailure("1962/01/01");
      testParseEyeFailure("31/01/1962");
      testParseEyeFailure("01/X1/1962");
      testParseEyeFailure("01/01/62");
      testParseEyeFailure("01/01/062");
      
      testParseEyeFailure("12/31/1975 ");
      testParseEyeFailure("12/31/1975 0");
      testParseEyeFailure("12/31/1975 0:");
      testParseEyeFailure("12/31/1975 0:00");
      testParseEyeFailure("12/31/1975 00:00 ");
      testParseEyeFailure("12/31/1975 24:00");
      testParseEyeFailure("12/31/1975 23:60");
      testParseEyeFailure("12/31/1975 49:49");
      testParseEyeFailure("12/31/1975 100:00");
      
    }
    
    public void testFormatSuccess(){
      testFormatSuccess(getDate(1962,1,1,10,0), "01/01/1962 10:00");
      testFormatSuccess(getDate(1962,1,1,0,0), "01/01/1962");
    }
    
    public void testFormatFailure(){
      testFormatFailure(getDate(1962,1,1,10,0), "01/01/1962 10:00 ");
      testFormatFailure(getDate(1962,1,1,0,0), "01/01/1962 ");
      testFormatFailure(getDate(1962,1,1,0,0), " 01/01/1962");
    }
    
    // PRIVATE
    
    private void testParseHand(String aInput, Date aExpectedDate ){
      DateConverterImpl converter = new DateConverterImpl();
      Date parsedDate = converter.parseHandFriendly(aInput, null, TimeZone.getDefault()); //possibly null return
      if(parsedDate == null) {
        fail("Unable to parse: " + aInput);
      }
      else {
        if( ! parsedDate.equals(aExpectedDate) ) {
          fail("Parsed into " + parsedDate + ", but was expecting " + aExpectedDate);
        }
      }
    }
    
    private void testParseHandFailure(String aInput){
      DateConverterImpl converter = new DateConverterImpl();
      Date parsedDate = converter.parseHandFriendly(aInput, null, TimeZone.getDefault()); //possibly null return
      if(parsedDate != null) {
        fail("Unexpectedly able to parse: " + aInput);
      }
    }
    
    private void testParseEye(String aInput, Date aExpectedDate ){
      DateConverterImpl converter = new DateConverterImpl();
      Date parsedDate = converter.parseEyeFriendly(aInput, null, TimeZone.getDefault()); //possibly null return
      if(parsedDate == null) {
        fail("Unable to parse: " + aInput);
      }
      else {
        if( ! parsedDate.equals(aExpectedDate) ) {
          fail("Parsed into " + parsedDate + ", but was expecting " + aExpectedDate);
        }
      }
    }
    
    private void testParseEyeFailure(String aInput){
      DateConverterImpl converter = new DateConverterImpl();
      Date parsedDate = converter.parseEyeFriendly(aInput, null, TimeZone.getDefault()); //possibly null return
      if(parsedDate != null) {
        fail("Unexpectedly able to parse: " + aInput);
      }
    }
    
    private void testFormatSuccess(Date aDate, String aExpected){
      DateConverterImpl converter = new DateConverterImpl();
      String formattedDate = converter.formatEyeFriendly(aDate, null, TimeZone.getDefault());
      if( ! formattedDate.equals(aExpected) ){
        fail("Expected " + Util.quote(aExpected) + ", but computed " + Util.quote(formattedDate));
      }
    }
    
    private void testFormatFailure(Date aDate, String aExpectedFailure){
      DateConverterImpl converter = new DateConverterImpl();
      String formattedDate = converter.formatEyeFriendly(aDate, null, TimeZone.getDefault());
      if( formattedDate.equals(aExpectedFailure) ){
        fail("Expected failure to match " + Util.quote(aExpectedFailure) + ", but computed " + Util.quote(formattedDate));
      }
    }
    
    private Date getDate(int aYear, int aMonth, int aDay, int aHour, int aMinute){
      Calendar calendar = new GregorianCalendar(aYear, aMonth - 1, aDay, aHour, aMinute);
      return calendar.getTime();
    }

    private void testFormatDateTime(String aDateTime, String aExpected){
      DateTime dt = new DateTime(aDateTime);
      DateConverter converter = new DateConverterImpl();
      String actual = converter.formatEyeFriendlyDateTime(dt, null);
      if(!actual.equals(aExpected)){
        fail("Actual:'"+ actual + "' Expected:'" + aExpected + "'");
      }
    }
    
    private void testParseEyeDateTime(String aInput, String aExpected){
      DateTime expected = new DateTime(aExpected);
      DateConverter converter = new DateConverterImpl();
      DateTime actual = converter.parseEyeFriendlyDateTime(aInput, null);
      if(!actual.equals(expected)){
        fail("Actual:" + actual + " Expected:" + aExpected);
      }
    }
    
    private void testParseHandDateTime(String aInput, String aExpected){
      DateTime expected = new DateTime(aExpected);
      DateConverter converter = new DateConverterImpl();
      DateTime actual = converter.parseHandFriendlyDateTime(aInput, null);
      if(actual == null){
        fail("Failed to parse the input '" + aInput + "'");
      }
      else if(!actual.equals(expected)){
        fail("Actual:" + actual + " Expected:" + aExpected);
      }
    }
}
