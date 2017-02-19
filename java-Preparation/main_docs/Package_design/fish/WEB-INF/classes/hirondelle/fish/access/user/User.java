package hirondelle.fish.access.user;

import java.util.regex.*;
import java.util.logging.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Validator;
import hirondelle.web4j.security.SafeText;
import static hirondelle.web4j.util.Consts.FAILS;

/**
 User for the Fish and Chips Club.
  
 <P>When first added, the User has a fixed, inconvenient password, which  
 they are encouraged to change. This is not enforced by the application, however.

 <P>The password is "hashed". 
 A one-way hash function ensures the password is NOT stored in cleartext.
 When the container enforces a security-constraint defined in <tt>web.xml</tt>, the container 
 must be instructed to call the exact same hash function (SHA-1), in order to match the database.

 <P>Performing the hash here, instead of in the database, provides independance from 
 database implementations of hash functions (or lack thereof). 

 <P>Note that a one-way <em>hash</em> function is used here, NOT an <em>encryption</em> 
 function: encrypted items are ultimately intended for decryption. Here, no decryption is ever attempted. 
 In fact, the whole point of using a hash function is that it is nearly impossible to 
 deduce the password from the hashed value itself.
*/
public final class User {

  /** Verify presence of hash function in current JRE.  */
  static {
    MessageDigest sha = null;
    try {
      sha = MessageDigest.getInstance("SHA-1");
    }
    catch (NoSuchAlgorithmException ex){
      throw new RuntimeException("Unable to hash passwords. MessageDigest class cannot find the SHA-1 hash function.");
    }
  }
  
  /**
   Constructor taking a password that is already hashed.
  
   <P>This constructor is used when retrieving from the database, where passwords are 
   already stored in a hashed form.
   
   @param aName user name (required), 6..50 characters, no spaces.
   @param aHashedPassword <em>hashed</em> user password (required), 6..50 characters, no spaces, and never the 
   same as the user name.
  */
  public User(SafeText aName, SafeText aHashedPassword) throws ModelCtorException {
    fName = aName;
    fHashedPassword = aHashedPassword;
    validateState();
  }
  
  /**
   Factory method for a new {@link User}, with a fixed, initial password. 
   
   <P><span class="highlight">In this implementation, the initial password is very
   long, and thus inconvenient to use. New users are encouraged to change it immediately, 
   upon first use. This is not enforced, however.</span> 
  */ 
  public static User forNewUserOrPasswordReset(SafeText aName) throws ModelCtorException {
    return new User(aName, SafeText.from(hash(MAGIC_INITIAL_PASSWORD)));
  }
  
  /**
   Factory method for a new {@link User}, reflecting a new password.
   
   <P>The arguments must pass the same constraints as {@link #User(SafeText, SafeText)}. 
  */
  public static User forPasswordChange(SafeText aName, SafeText aClearTextPassword) throws ModelCtorException {
    User temp = new User(aName, aClearTextPassword); //will throw ex if fails
    return new User(aName, SafeText.from(hash(aClearTextPassword.getRawString())));
  }
  
  /** Return the user name passed to the constructor.  */
  public SafeText getName() {  
    return fName; 
  }
  
  /** Return the <em>hashed</em> password (never cleartext).  */
  public SafeText getPassword() {  
    return fHashedPassword;  
  }
  
  /**
   Return <tt>true</tt> only if the password matches the initial, reset value.
   
   <P>Passwords which match the initial, reset value should be changed by the end user.
  */
  public boolean isResetValue(){
    return hash(MAGIC_INITIAL_PASSWORD).equalsIgnoreCase(fHashedPassword.getRawString());
  }
  
  /** Intended for debugging only. The return value will mask the password. */
  @Override public String toString() {
    return hirondelle.fish.access.user.User.class + " User Name : " + fName + " Password : ****"; 
  }

  @Override public boolean equals( Object aThat ) {
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ){
      User that = (User) aThat;
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
   
  //PRIVATE//
  private final SafeText fName;
  private final SafeText fHashedPassword;
  private int fHashCode;
  
  /**
   Not public, since could be used in an attack, if visible in javadoc.
   
   <P>HIGHLY RECOMMENDED that this value be changed for your application, but that the basic idea be 
   preserved : this initial/reset password should be inconvenient to type, so that the user will 
   be strongly encouraged to change it as soon as possible. 
  */
  private static final String MAGIC_INITIAL_PASSWORD = "changemetosomethingalotmoreconvenienttotype";
  private static final Pattern ACCEPTED_PATTERN = Pattern.compile("(?:\\S){6,50}");
  private static final Logger fLogger = Util.getLogger(User.class);

  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    Validator validPattern = Check.pattern(ACCEPTED_PATTERN);
    if ( FAILS == Check.required(fName, validPattern) ) {
      ex.add("Name is required, 6..50 chars, no spaces.");
    }
    if ( FAILS == Check.required(fHashedPassword, validPattern)) {
      ex.add("Password is required, 6..50 chars, no spaces.");
    }
    if( fName != null && fHashedPassword != null ) {
      if( fName.getRawString().equalsIgnoreCase(fHashedPassword.getRawString()) ){
        ex.add("Password cannot be the same as the user name.");
      }
    }
    if ( ! ex.isEmpty() ) throw ex;
  }
   
  private Object[] getSignificantFields(){
    return new Object[] {fName, fHashedPassword};
  }

  /**
   The static initializer of this class will barf if the hash function is not present.
  */
  private static String hash(String aCleartext) {
    String result = null;
    
    MessageDigest sha = null;
    try {
      sha = MessageDigest.getInstance("SHA-1");
    }
    catch (NoSuchAlgorithmException ex){
      fLogger.severe("Cannot find SHA-1 hash function.");
    }

    if (sha != null){
      byte[] digest =  sha.digest( aCleartext.getBytes() );
      result = hexEncode(digest);
    }
    else {
      result = aCleartext;
    }
    return result;
  }
  
  /**
   The byte[] returned by MessageDigest does not have a nice
   textual representation, so some form of encoding is usually performed.
  
   This implementation follows the example of David Flanagan's book
   "Java In A Nutshell", and converts a byte array into a String
   of hex characters.
  */
  private static String hexEncode( byte[] aInput){
    StringBuffer result = new StringBuffer();
    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
    for ( int idx = 0; idx < aInput.length; ++idx) {
      byte b = aInput[idx];
      result.append( digits[ (b&0xf0) >> 4 ] );
      result.append( digits[ b&0x0f] );
    }
    return result.toString();
  }  
  
  /** Informal test harness. Change to public to exercise. */
  private static void main(String[] args){
    System.out.println("Hash: " + hash("testtest"));
  }
}
