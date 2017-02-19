package hirondelle.fish.main.member;

import java.util.*;
import java.io.FileNotFoundException;
import javax.servlet.ServletException;
import junit.framework.*;
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import hirondelle.web4j.model.AppException;
import hirondelle.fish.test.TESTAll;
import hirondelle.fish.test.doubles.FakeRequest;
import hirondelle.fish.test.doubles.FakeResponse;
import hirondelle.web4j.request.RequestParserImpl;
import static hirondelle.web4j.action.ActionImpl.ITEMS_FOR_LISTING;
import static hirondelle.web4j.action.ActionImpl.ITEM_FOR_EDIT;
import static hirondelle.web4j.action.ActionImpl.ERRORS;
import hirondelle.web4j.model.MessageList;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.action.Action;

/**
 JUnit tests for {@link MemberAction}.
  
 <P>To run this class, the <b>classpath must contain the database driver</b>.
 See {@link TESTAll#initControllerIfNeeded()} as well. 
 
 <P>Actions typically use a DAO. When testing, some prefer to use a 
 fake DAO instead of the real one. This is done to eliminate side effects, such that 
 the tests can be easily repeated. It may also significantly increase execution speed.
*/
public class TESTMemberAction extends TestCase {

  /** Initialize the environment and run the test cases.   */
  public static void main(String args[]) throws ServletException, FileNotFoundException {
    TESTAll.setRootDirectory();
    TESTAll.actionsUseFakeDAOs(true);
    TESTAll.initControllerIfNeeded();
    
    String[] testCaseName = {TESTMemberAction.class.getName()};
    junit.textui.TestRunner.main(testCaseName);
  }

  public TESTMemberAction(String aName) throws ServletException, FileNotFoundException {
    super(aName);
  }
  
  /** 
   This test is executed first. 
   Other tests that follow assume that particular records already exist.
  */
  public void testAdd() throws  AppException {
    testEditActionFails("Name=B&IsActive=true&Disposition=1", ADD);
    testEditActionFails("Name=&IsActive=true&Disposition=1", ADD);
    testEditActionFails("Name=Bob&IsActive=blah&Disposition=1", ADD); 
    testEditActionFails("Name=Bob&IsActive=true&Disposition=0", ADD);
    
    testEditActionSucceeds("Name=TeddySuctionCup&IsActive=true&Disposition=1", ADD);
    testEditActionSucceeds("Name=Carol&IsActive=false&Disposition=3", ADD);
    testEditActionSucceeds("Name=Alice&IsActive=&Disposition=3", ADD);
    
    testEditActionFails("Name=Carol&IsActive=true&Disposition=1", ADD); //duplicate name
  }
  
  public void testList() throws AppException {
    HttpServletRequest req = FakeRequest.forGET(LIST, NO_PARAMS);
    HttpServletResponse resp = new FakeResponse();
    MemberAction action = new MemberAction(new RequestParserImpl(req, resp));
    
    ResponsePage responsePage = action.execute(); 
    Object item = req.getAttribute(ITEMS_FOR_LISTING);
    assertTrue(item != null);
    assertTrue(item instanceof List);
    List<Member> members = (List<Member>)item;
    assertTrue( members.size() > 0 );
    //log(item);
  }
  
  public void testFetch() throws AppException {
    HttpServletRequest req = FakeRequest.forGET(FETCH, "Id=3");
    HttpServletResponse resp = new FakeResponse();
    MemberAction action = new MemberAction(new RequestParserImpl(req, resp));
    
    ResponsePage responsePage = action.execute(); 
    Object item = req.getAttribute(ITEM_FOR_EDIT);
    //log(item);
    assertTrue(item != null);
    assertTrue(item instanceof Member);
  }
  
  
  public void testChange() throws AppException {
    testEditActionSucceeds("Id=2&Name=Carol&IsActive=false&Disposition=2", CHANGE);
    testEditActionSucceeds("Id=2&Name=Carol&IsActive=true&Disposition=2", CHANGE);
    testEditActionSucceeds("Id=2&Name=Carol&IsActive=false&Disposition=2", CHANGE);
    testEditActionSucceeds("Id=2&Name=Carol&IsActive=true&Disposition=1", CHANGE);
    testEditActionSucceeds("Id=2&Name=Carol&IsActive=false&Disposition=3", CHANGE);
    
    testEditActionFails("Id=2&Name=B&IsActive=false&Disposition=3", CHANGE); //name too short
    testEditActionFails("Id=2&Name=&IsActive=false&Disposition=3", CHANGE); //missing name
    testEditActionFails("Id=2&Name=Bobby&IsActive=false&Disposition=xyz", CHANGE); //invalid code
  }

  public void testDelete() throws AppException {
    testEditActionSucceeds("Id=1", DELETE);
  }
  
  // PRIVATE //
  
  /**
   The servlet path is what maps a URL to a servlet, and in turn to an Action.
   In this case, this 'base' servlet path has various extensions added to it to 
   denote specific operations. 
  */
  private static final String BASE_SERVLET_PATH = "/main/member/MemberAction.";
  private static final String ADD = BASE_SERVLET_PATH + "add";
  private static final String LIST = BASE_SERVLET_PATH + "list";
  private static final String FETCH = BASE_SERVLET_PATH + "fetchForChange";
  private static final String CHANGE = BASE_SERVLET_PATH + "change";
  private static final String DELETE = BASE_SERVLET_PATH + "delete";
  
  private static final String NO_PARAMS = null;
  
  private static final boolean ARE_PRESENT = true;
  private static final boolean ARE_ABSENT = false;
  
  private static final boolean SUCCEEDS = true;
  private static final boolean FAILS = false;
  
  private static final boolean POST = true;
  
  private void log(Object aMessage){
    System.out.println(String.valueOf(aMessage));
  }
  
  private void testEditActionSucceeds(String aQueryString, String aServletPath) throws AppException {
    testAction(SUCCEEDS, aQueryString, aServletPath, POST);
  }
  
  private void testEditActionFails(String aQueryString, String aServletPath) throws AppException {
    testAction(FAILS, aQueryString, aServletPath, POST);
  }
  
  private void testAction(boolean aSucceeds, String aQueryString, String aServletPath, boolean aNeedsPost) throws AppException {
    HttpServletRequest req = null;
    if (aNeedsPost) {
      req = FakeRequest.forPOST(aServletPath, aQueryString);
    }
    else {
      req = FakeRequest.forGET(aServletPath, aQueryString);
    }
    HttpSession session = req.getSession(true); //links request to session
    assertTrue(session != null);
    
    HttpServletResponse resp = new FakeResponse();
    MemberAction action = new MemberAction(new RequestParserImpl(req, resp));
    
    if(aSucceeds) {
      assertActionSucceeds(req, action);
    }
    else {
      assertActionFails(req, action);
    }
  }

  private void assertActionSucceeds(HttpServletRequest aRequest, Action aAction) throws AppException {
    try {
      aAction.execute();
      assertErrors(ARE_ABSENT, aRequest);
    }
    catch(RuntimeException ex){
      fail("Action unexpectedly failed: " + ex);
    }
  }
  
  private void assertActionFails(HttpServletRequest aRequest, Action aAction) throws AppException {
    try {
      aAction.execute();
      assertErrors(ARE_PRESENT, aRequest);
    }
    catch(RuntimeException ex){
      //another failure branch - do nothing
    }
  }
  
  private void assertErrors(boolean aArePresent, HttpServletRequest aRequest){
    HttpSession existingSession = aRequest.getSession(false);
    assertNotNull(existingSession);
    Object errorList = existingSession.getAttribute(ERRORS);
    if( aArePresent ) {
      assertNotNull(errorList);
      assertTrue(errorList instanceof MessageList);
    }
    else {
      if ( errorList != null ){
        log("params: " + aRequest.getQueryString() + "errors " + errorList);
      }
      assertNull(errorList);
    }
  }
}
