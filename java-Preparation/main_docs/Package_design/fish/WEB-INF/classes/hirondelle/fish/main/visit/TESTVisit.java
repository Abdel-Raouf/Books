package hirondelle.fish.main.visit;

import junit.framework.*;
import java.io.FileNotFoundException;
import java.util.*;
import javax.servlet.ServletException;
import hirondelle.fish.test.TESTAll;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;

/**
  JUnit test cases for {@link Visit#equals(Object)}.
  <P>
  These tests need the database driver to be present on the classpath, to allow code tables
  to function as usual.
 */
public final class TESTVisit extends TestCase {

  /** Run the test cases. */
  public static void main(String args[]) throws FileNotFoundException, ServletException {
    TESTAll.setRootDirectory();
    TESTAll.initControllerIfNeeded();
    String[] testCaseName = {TESTVisit.class.getName()};
    junit.textui.TestRunner.main(testCaseName);
  }

  public TESTVisit(String aName) {
    super(aName);
  }

  // TEST CASES //

  public void testEquals() {
    testEqualsFor(VISIT_A, "100", "1", new Date(107, 0, 1), "This is the day.");
    testEqualsFor(VISIT_A, "200", "1", new Date(107, 0, 1), "This is the day.");
    testEqualsFor(VISIT_A, null, "1", new Date(107, 0, 1), "This is the day.");
  }

  public void testNotEqualToNull() {
    assertFalse(VISIT_A.equals(null));
  }

  public void testSubclass() {
    assertTrue(VISIT_A.equals(VISIT_B));
  }

  public void testNotEquals() {
    testNotEqualsFor(VISIT_A, "100", "2", new Date(107, 0, 1), "This is the day.");
    testNotEqualsFor(VISIT_A, "100", "1", new Date(106, 0, 1), "This is the day.");
    testNotEqualsFor(VISIT_A, "100", "1", new Date(107, 0, 2), "This is the day.");
    testNotEqualsFor(VISIT_A, "100", "1", new Date(107, 0, 1), "This is the day blah.");
    testNotEqualsFor(VISIT_A, "100", "1", new Date(107, 0, 1), "Thiss is the day.");
  }

  // FIXTURE //

  protected void setUp() {
    try {
      VISIT_A = new Visit(Id.from("100"), Id.from("1"), new Date(107, 0, 1), new SafeText(
      "This is the day."));
      VISIT_B = new GentlemanCaller(Id.from("200"), Id.from("1"), new Date(107, 0, 1), new SafeText(
      "This is the day."));
    }
    catch (ModelCtorException ex) {
      // nothing...
    }
  }

  protected void tearDown() {
    // empty
  }

  // PRIVATE //
  private Visit VISIT_A;

  private GentlemanCaller VISIT_B;

  private void testEqualsFor(Visit aVisit, String aId, String aRestoId, Date aDate, String aComment) {
    Visit testVisit = null;
    Id visitId = (aId != null ? Id.from(aId) : null);
    try {
      testVisit = new Visit(visitId, Id.from(aRestoId), aDate, new SafeText(aComment));
    }
    catch (ModelCtorException ex) {
      throw new RuntimeException(ex);
    }
    // log("aVisit: " + aVisit);
    // log("testVisit: " + testVisit);
    assertTrue(aVisit.equals(testVisit));
    assertTrue(aVisit.hashCode() == testVisit.hashCode());
  }

  private void testNotEqualsFor(Visit aVisit, String aId, String aRestoId, Date aDate,
  String aComment) {
    Visit testVisit = null;
    Id visitId = (aId != null ? Id.from(aId) : null);
    try {
      testVisit = new Visit(visitId, Id.from(aRestoId), aDate, new SafeText(aComment));
    }
    catch (ModelCtorException ex) {
      throw new RuntimeException(ex);
    }
    // log("A: " + aVisit);
    // log("B: " + testVisit);
    // log("Equal: " + aVisit.equals(testVisit));
    assertTrue(!aVisit.equals(testVisit));
  }

  private static void log(Object aMsg) {
    System.out.println(String.valueOf(aMsg));
  }

  private static final class GentlemanCaller extends Visit {
    public GentlemanCaller(Id aId, Id aRestaurantId, Date aLunchDate, SafeText aMessage)
      throws ModelCtorException {
      super(aId, aRestaurantId, aLunchDate, aMessage);
    }
  }
}
