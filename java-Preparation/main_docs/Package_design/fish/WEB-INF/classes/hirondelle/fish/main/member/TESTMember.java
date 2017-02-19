package hirondelle.fish.main.member;

import java.io.FileNotFoundException;
import javax.servlet.ServletException;
import junit.framework.*;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.fish.test.TESTAll;

/**
 JUnit tests for {@link Member}. 
 Most tests for a Model Object are tests on the validation logic.
  
 <P>To run this class, the <b>classpath must contain the database driver</b>.
 This is because <tt>Member</tt> uses code tables, and code tables are initialized  
 using the database. See {@link TESTAll#initControllerIfNeeded()}. 
*/
public final class TESTMember extends TestCase {

  /** Initialize the environment and run the test cases.   */
  public static void main(String args[]) throws ServletException, FileNotFoundException {
    TESTAll.setRootDirectory();
    TESTAll.initControllerIfNeeded();
    
    String[] testCaseName = {TESTMember.class.getName()};
    junit.textui.TestRunner.main(testCaseName);
  }

  public TESTMember(String aName) {
    super(aName);
  }

  /**
   Almost all of the logic of a Model Object is in its validation, which is in turn called 
   in its constructor. 
   
   <P>Hence, this class only exercises the success and failure of the constructor. 
   There doesn't seem to be much value in testing the other public methods.
  */
  public void testConstruction()  {
    testCtorSuccess("1", "Bob Smithers", true, "1");
    testCtorSuccess("A", "Bob Smithers", true, "1");
    testCtorSuccess("123", "Bob Smithers", true, "1");
    testCtorSuccess("1234567891013213216541", "Bob Smithers", true, "1");
    testCtorSuccess(null, "Bob Smithers", true, "1");

    testCtorSuccess("1", "Bo", true, "1");
    testCtorSuccess("1", "Bob Smithersaslkdjf aslkdjf ;laksld ", true, "1");

    testCtorSuccess("1", "Bob Smithers", false, "1");

    testCtorSuccess("1", "Bob Smithers", false, "4");

   testCtorFailure(null, null, null, null);
   testCtorFailure("", null, null, null);
   testCtorFailure(null, null, false, "1");
   testCtorFailure(null, "", false, "1");
   testCtorFailure(null, " ", false, "1");
   testCtorFailure(null, "A", false, "1");
   testCtorFailure(null, "A ", false, "1");
   
   testCtorFailure(null, "Bob Smithers", false, null);
  }

  // PRIVATE //

  private void testCtorSuccess(String aId, String aName, Boolean aIsActive, String aDisposition) {
    Id id = (aId != null ? Id.from(aId) : null);
    SafeText name = (aName != null ? new SafeText(aName) : null);
    Id disposition = (aDisposition != null ? Id.from(aDisposition) : null);
    try {
      Member member = new Member(id, name, aIsActive, disposition);
    }
    catch (ModelCtorException ex) {
      fail("Failed to construct Member.");
    }
  }

  private void testCtorFailure(String aId, String aName, Boolean aIsActive, String aDisposition) {
    Id id = (aId != null ? Id.from(aId) : null);
    SafeText name = (aName != null ? new SafeText(aName) : null);
    Id disposition = (aDisposition != null ? Id.from(aDisposition) : null);
    try {
      Member member = new Member(id, name, aIsActive, disposition);
      fail("Unexpectedly succeeded in constructing Member.");
    }
    catch (ModelCtorException ex) {
      //do nothing !
    }
  }
}
