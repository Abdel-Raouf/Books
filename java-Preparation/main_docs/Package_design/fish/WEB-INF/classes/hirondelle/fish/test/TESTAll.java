package hirondelle.fish.test;

import java.io.FileNotFoundException;
import javax.servlet.ServletException;
import junit.framework.*;
import hirondelle.web4j.Controller;
import hirondelle.fish.test.doubles.FakeServletConfig;
import hirondelle.fish.test.doubles.FakeServletContext;
import hirondelle.web4j.config.TESTDateConverterImpl;
import hirondelle.fish.main.visit.TESTVisit;
import hirondelle.fish.main.member.TESTMember;
import hirondelle.fish.main.member.TESTMemberAction;
import hirondelle.fish.main.member.TESTMemberDAO;
import hirondelle.fish.test.doubles.FakeDAOBehavior;
import hirondelle.fish.main.member.MemberDAO;

/**
 Runs all <a href="http://www.junit.org">JUnit</a> tests defined for this application.
 
<P><span class='highlight'>To run the tests in the example app, you must perform 
 two edits to reflect your environment</span>:
 <ul>
  <li>set the root directory of the web app in {@link #setRootDirectory()}
  <li>set the credentials for database access in {@link hirondelle.fish.test.doubles.FakeConnectionSrc}
</ul>
 
<P>In addition, these tests <b>require the database driver to be present on the classpath.</b>
 
 <P>The recommended style is to place test classes in the same directory as the classes being
 tested. Such a style will follow the package-by-feature approach, where all items related to a 
 single feature are placed in a single directory - <em>including the unit tests</em>.
 
 <P>These tests are a development tool, and are not typically executed in a deployment
 environment.
*/
public final class TESTAll {

  /** 
   Run all JUnit tests in the application.
   
   <P>This method calls {@link #initControllerIfNeeded()}. 
  */
  public static void main(String args[]) throws ServletException, FileNotFoundException {
    setRootDirectory();
    initControllerIfNeeded();
    
    String[] testCaseName = {TESTAll.class.getName()};
    junit.textui.TestRunner.main(testCaseName);
    //you may want to set logging levels here
  }

  /** 
   Alter a System property named 
   {@value hirondelle.fish.test.doubles.FakeDAOBehavior#USE_FAKE_DAOS} 
   
   <P>That system property will control whether or not an Action will 
   use a fake DAO or a real DAO. See {@link MemberDAO#getInstance()} for 
   an illustration. 
   
    @param aToggle controls whether or not Actions should use fake DAOs. 
    If <tt>true</tt>, then a System property named 
    {@value hirondelle.fish.test.doubles.FakeDAOBehavior#USE_FAKE_DAOS} 
    is added with value <tt>true</tt>; if <tt>false</tt>, then the same 
    property is removed. 
  */
  public static void actionsUseFakeDAOs(boolean aToggle){
    if( aToggle ) {
      System.setProperty(FakeDAOBehavior.USE_FAKE_DAOS, "true");
    }
    else {
      System.clearProperty(FakeDAOBehavior.USE_FAKE_DAOS);
    }
  }
  
  /**
   Initialize the WEB4J Controller using a {@link FakeServletConfig}.
   
  <P><b>This method requires the database driver to be present on the classpath.</b>
  
   <P>This initialization will allow tests for Models, DAOs, and Actions to be run as 
   if they are in the regular runtime enviroment. 
    
   <P>This method can be called repeatedly. If called more than once, then it is a 
   no-operation. Thus, this method can be called both by this test suite, and 
   by the individual unit tests. This allows unit tests to run either 
   one at a time, or as part of this suite. 
   
   <P>Calling this method mimics the regular runtime environment by passing a {@link FakeServletConfig}
   to the {@link Controller#init(javax.servlet.ServletConfig)} method. This in turn 
   uses a {@link hirondelle.fish.test.doubles.FakeConnectionSrc} for fetching connections
   without using JNDI.
  */
  public static void initControllerIfNeeded() throws ServletException, FileNotFoundException {
    if (!fIsControllerInitialized) {
      initController();
    }
  }
  
  /**
   Set a System property needed by fake objects to a hard-coded value.
   
   <P>This System property must be set when using the fake objects.
   An alternative to calling this method is to simply define a <tt>-D</tt>
   argument to the JRE.
    
   <P>See {@link FakeServletContext#ROOT_DIRECTORY} for 
   more information.
  */
  public static void setRootDirectory(){
    System.setProperty(FakeServletContext.ROOT_DIRECTORY, "C:\\myname\\projects\\fish");
  }

  /**
    Return a {@link TestSuite} for all unit tests in the application. 
  */
  public static Test suite() {
    TestSuite suite = new TestSuite("All JUnit Tests");

    /*
     Model Objects.
      
     Some models will have a dependency on proper configuration of 
     code tables (which means ensuring the Controller is initialized). 
    */
    suite.addTest(new TestSuite(TESTDateConverterImpl.class));
    suite.addTest(new TestSuite(TESTVisit.class));
    suite.addTest(new TestSuite(TESTMember.class));

    /*
     Actions.
     
     Here, actions use fake DAOs in order to avoid side effects, and  
     ensure easy repeatability. 
    */
    actionsUseFakeDAOs(true);
    suite.addTest(new TestSuite(TESTMemberAction.class));

    /*
     DAOs. 
     
     Tests both real and fake DAOs.
     Fake DAOs can be tested for various behaviors upon failure. 
     Here, that behavior is controlled using various System properties.
     Testing the DAOs <i>last</i> is convenient, since those System settings
     will not affect later tests.
    */
    suite.addTest(new TestSuite(TESTMemberDAO.class));

    return suite;
  }

  // PRIVATE //
  private static boolean fIsControllerInitialized = false;

  private static void initController() throws ServletException, FileNotFoundException {
    Controller controller = new Controller();
    controller.init(new FakeServletConfig());
    fIsControllerInitialized = true;
  }
}
