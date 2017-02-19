package hirondelle.fish.main.member;

import java.io.FileNotFoundException;
import javax.servlet.ServletException;
import junit.framework.*;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.fish.test.TESTAll;
import hirondelle.fish.test.doubles.FakeDAOBehavior;
import static hirondelle.fish.test.doubles.FakeDAOBehavior.DbOperation;
import static hirondelle.fish.test.doubles.FakeDAOBehavior.DbOperationResult;

/** 
 JUnit tests for {@link MemberDAO} and {@link MemberDAOFake}.
 
<P>To run this class, the <b>classpath must contain the database driver</b>.
 See {@link TESTAll#initControllerIfNeeded()} as well.
  
 <P>These unit tests exercise the basic sanity of add/fetch/change/delete operations.
 There are other operations defined in {@link MemberDAO}, but they aren't tested by this 
 class. Transactions are not used to rollback the changes to the database.
 
 <P>When all tests succeed, any records created by these tests are automatically deleted towards the end. 
 If the tests <em>fail</em>, however, then it's usually necessary to <em>manually</em> delete  
 records created by these tests.
*/
public class TESTMemberDAO extends TestCase {

  /** Initialize the environment and run the test cases.  */
  public static void main(String args[]) throws ServletException, FileNotFoundException {
    TESTAll.setRootDirectory();
    TESTAll.initControllerIfNeeded();
    
    String[] testCaseName = { TESTMemberDAO.class.getName() };
    junit.textui.TestRunner.main(testCaseName);
  }

  public TESTMemberDAO(String aName) {
    super(aName);
  }

  // TEST CASES //
  
  /** Test the real DAO.  */
  public void testRealDAO() throws DAOException, ModelCtorException {
    MemberDAO realDAO = new MemberDAO();
    testForSuccess(realDAO);
  }
  
  /** Test the fake DAO, without configuring explicit failures for any of its operations. */
  public void testFakeDAO() throws DAOException, ModelCtorException {
    MemberDAO fakeDAO = new MemberDAOFake();
    testForSuccess(fakeDAO);
  }

  /** 
   Test the fake DAO, with configuring explicit failures for all its operations.
   One of the advantages of using 
  */
  public void testFakeDAOFailure() throws DAOException, ModelCtorException {
    FakeDAOBehavior.setBehavior(DbOperation.FetchForChange, DbOperationResult.ThrowDAOException);
    FakeDAOBehavior.setBehavior(DbOperation.Add, DbOperationResult.ThrowDuplicateException);
    FakeDAOBehavior.setBehavior(DbOperation.Change, DbOperationResult.ThrowDuplicateException);
    FakeDAOBehavior.setBehavior(DbOperation.Delete, DbOperationResult.ThrowDAOException);
    FakeDAOBehavior.setBehavior(DbOperation.List, DbOperationResult.ThrowDAOException);
    
    MemberDAO fakeDAO = new MemberDAOFake();
    testFakeForFailures(fakeDAO);
  }
  
  //PRIVATE//
  
  private void testForSuccess(MemberDAO aMemberDAO) throws DAOException, ModelCtorException {
    //add
    Member memberOne = new Member(null, new SafeText("Bob Smithers"), Boolean.TRUE, Id.from("4") );
    Id idOne = aMemberDAO.add(memberOne); 
    
    //fetch
    Member memberOneFetched = aMemberDAO.fetch(idOne);
    assertNotNull(memberOneFetched);
    assertTrue(memberOneFetched.equals(memberOne));
    //log("memberOneFetched: " + memberOneFetched);
    
    //change
    Member memberTwo = new Member(idOne, new SafeText("Bob Smitherspoon"), Boolean.TRUE, Id.from("4") );
    aMemberDAO.change(memberTwo);
    Member memberTwoFetched = aMemberDAO.fetch(idOne);
    assertNotNull(memberTwoFetched);
    assertTrue(! memberTwoFetched.equals(memberOne));
    assertTrue(memberTwoFetched.equals(memberTwo));
    
    //delete
    aMemberDAO.delete(idOne);
    memberTwoFetched = aMemberDAO.fetch(idOne);
    assertNull(memberTwoFetched);
  }
  
  private void testFakeForFailures(MemberDAO aMemberDAO) throws ModelCtorException, DAOException {
    //add
    Member memberOne = new Member(null, new SafeText("Bob Smithers"), Boolean.TRUE, Id.from("4") );
    try {
      Id id = aMemberDAO.add(memberOne);
      fail("Should have failed.");
    }
    catch(DuplicateException ex){
      //should happen - do nothing
    }
    
    //change
    Member memberTwo = new Member(Id.from("13"), new SafeText("Bob Smitherspoon"), Boolean.TRUE, Id.from("4") );
    try {
      aMemberDAO.change(memberTwo);
      fail("Should have failed.");
    }
    catch(DuplicateException ex){
      //should happen - do nothing
    }
    
    //delete
    try {
      aMemberDAO.delete(Id.from("13"));
      fail("Should have failed");
    }
    catch(DAOException ex){
      //should happen - do nothing
    }
    
    //list
    try {
      aMemberDAO.list();
      fail("Should have failed");
    }
    catch (DAOException ex){
      //should happen - do nothing
    }
    
    //fetch
    try {
      aMemberDAO.fetch(Id.from("13"));
      fail("Should have failed");
    }
    catch (DAOException ex){
      //should happen - do nothing
    }
  }
}
