package hirondelle.fish.test.doubles;

import java.sql.SQLException;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.util.Util;

/** 
 Change the exception behavior of fake DAOs.
 One of the advantages of using fake DAOs is that they can be made to 
 throw specific exceptions when desired. 
 By default, fake DAO operations will always succeed, in the sense of not throwing 
 an explicit exception. 
 
 <P>You control the behavior of a fake DAO using these methods :
<ul>
 <li>call {@link #setBehavior(DbOperation, DbOperationResult)} to specify the desired behavior 
 for a given operation. If you don't call this method, then by default the fake DAO operation 
 will succeed.
 <li>call {@link #possiblyThrowExceptionFor(DbOperation)} on the first line of your fake 
 DAO method. If the operation has been set to fail, then an exception will be thrown. 
 If not, then the operation will complete successfully.
</ul>

 <P>For an illustration, see {@link hirondelle.fish.main.member.MemberDAOFake} and 
 {@link hirondelle.fish.main.member.TESTMemberDAO}.
  
<P><b>Implementation Note</b><br>
 This class uses simple <tt>System</tt> properties to store the desired behavior. Such an implementation 
 is suitable only for a single threaded environment.
*/
public final class FakeDAOBehavior {

  /**
   Name of a <tt>System</tt> property used to swap implementations 
   between real and fake DAOs. When the property has the value <tt>true</tt>,
   then fake DAOs are used. See 
   {@link hirondelle.fish.main.member.MemberDAOFake} for an illustration.
   
   Value - {@value}.
  */
  public static final String USE_FAKE_DAOS = "useFakeDAOs";
  
  /**
   Enumeration of the basic DAO operations.
  */
  public static enum DbOperation {
    Add("FakeDAOAdd"), 
    Change("FakeDAOChange"), 
    Delete("FakeDAODelete"), 
    FetchForChange("FakeDAOFetchForChange"), 
    List("FakeDAOList");
    public String toString(){
      return fName;
    }
    private DbOperation(String aName){
      fName = aName;
    }
    private String fName;
  }
  
  /**  Enumeration of how a basic DAO operation may succeed or fail.  */
  public static enum DbOperationResult {Succeed, ThrowDAOException, ThrowDuplicateException}
  
  /**
   Possibly throw an exception for the given operation.
   
   <P>An exception is thrown only if it has been indicated by calling 
   {@link #setBehavior(DbOperation, DbOperationResult)}.
  */
  public static void possiblyThrowExceptionFor(DbOperation aOperation) throws DAOException, DuplicateException {
    DbOperationResult opResult = getOperationResult(aOperation);
    if (FakeDAOBehavior.DbOperationResult.Succeed == opResult) return;
    if (FakeDAOBehavior.DbOperationResult.ThrowDAOException == opResult) throw new DAOException(FORCED_FAILURE, FORCED_EXCEPTION);
    if (FakeDAOBehavior.DbOperationResult.ThrowDuplicateException == opResult) throw new DuplicateException(FORCED_FAILURE, FORCED_EXCEPTION);
  }
  
  /** Set the desired behavior of a fake DAO operation.  */
  public static void setBehavior(DbOperation aDbOperation, DbOperationResult aDbOperationResult){
    System.setProperty(aDbOperation.toString(), aDbOperationResult.toString() ); 
  }
  
  // PRIVATE //
  private static final String FORCED_FAILURE = "Forced failure in fake DAO.";
  private static final Throwable FORCED_EXCEPTION = new SQLException(FORCED_FAILURE);

  private static DbOperationResult getOperationResult(DbOperation aOperation){
    DbOperationResult result = DbOperationResult.Succeed; //default value, if absent
    String property = System.getProperty(aOperation.toString());
    if ( Util.textHasContent(property)) {
      result = DbOperationResult.valueOf(property);
    }
    return result;
  }
}
