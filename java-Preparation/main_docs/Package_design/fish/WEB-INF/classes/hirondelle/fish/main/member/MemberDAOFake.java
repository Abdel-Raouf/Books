package hirondelle.fish.main.member;

import java.util.*;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import static hirondelle.fish.test.doubles.FakeDAOBehavior.*;
import hirondelle.web4j.util.Util;

/**
 Test double for {@link MemberDAO}.
 
 <P>This is a 'fake' class is intended for unit testing only. This implementation 
 stores its data in memory, using a static member of this class. 
 This class is safe for access from multiple threads. 
 
 <P>Note: this class uses <tt>synchronized</tt> to ensure thread 
 safety. However, this may not really be necessary in many cases. 
 It's only really necessary when your testing involves more than a single
 thread of execution. When running stand-alone unit tests, there is usually 
 only one such thread. When using tools such as HttpUnit, however, 
 which throw multiple HTTP requests at your app, then 'synchronized' is required.   
 
 <P>This class goes to some lengths to mimic the real behavior as closely as possible.
 For example, the items returned by {@link #list()} are sorted correctly using a specific 
 <tt>Comparator</tt>. In addition, there is a mechanism for forcing particular 
 exceptions to occur (important for exercising all branches.) 
*/
public class MemberDAOFake extends MemberDAO {
  
  /*
   Note : javadoc is not provided for these methods, since they are 
   designed to behave the same as the superclass. Links to the overridden 
   method will generated automatically by the javadoc tool. 
  */
  
  @Override List<Member> list() throws DAOException {
    possiblyThrowExceptionFor(DbOperation.List);
    synchronized (fMembers){
      ArrayList<Member> result =  new ArrayList<Member>(fMembers.values());
      Collections.sort(result, sortByMemberName());
      return result;
    }
  }

  @Override public Member fetch(Id aMemberId) throws DAOException {
    possiblyThrowExceptionFor(DbOperation.FetchForChange);
    synchronized (fMembers){
      return fMembers.get(aMemberId);
    }
  }

  @Override Id add(Member aMember) throws DAOException, DuplicateException {
    possiblyThrowExceptionFor(DbOperation.Add);
    synchronized (fMembers){
      for(Member existingMember:  fMembers.values()){
        if ( existingMember.getName().equals(aMember.getName())){
          throw new DuplicateException("Duplicate name: " + Util.quote(aMember.getName()), new Throwable());
        }
      }
      Id id = getNewId();
      //need to attach the id to the member in two ways here
      Member member = getMemberWithId(aMember, id);
      fMembers.put(id, member);
      return id;
    }
  }

  @Override boolean change(Member aMember) throws DAOException, DuplicateException {
    possiblyThrowExceptionFor(DbOperation.Change);
    synchronized (fMembers){
      Member originalMember = fMembers.put(aMember.getId(), aMember);
      return originalMember != null;
    }
  }

  @Override int delete(Id aMemberId) throws DAOException {
    possiblyThrowExceptionFor(DbOperation.Delete);
    Member existingMember = null;
    synchronized (fMembers){
      existingMember = fMembers.remove(aMemberId);
    }
    return existingMember != null ? 1 : 0;
  }
  
  @Override Integer getNumActiveMembers() throws DAOException {
    possiblyThrowExceptionFor(DbOperation.List);
    int result = 0;
    for (Member member: fMembers.values()){
      if(member.getIsActive()){
        ++result;
      }
    }
    return result;
  }
  
  // PRIVATE //
  private static final Map<Id, Member> fMembers = new LinkedHashMap<Id, Member>();
  private static Integer fIdGenerator = 0;
  
  /** Define a total sort of Member objects. */
  private Comparator<Member> sortByMemberName(){
    final int EQUAL = 0;
    return new Comparator<Member>() {
      public int compare(Member aThis, Member aThat) {
        if (aThis == aThat) return EQUAL;
        
        int comparison = aThis.getName().compareTo(aThat.getName());
        if(comparison != EQUAL) return comparison;
        
        comparison = aThis.getIsActive().compareTo(aThat.getIsActive());
        if(comparison != EQUAL) return comparison; 
        
        comparison = aThis.getDisposition().getId().compareTo(aThat.getDisposition().getId());
        if(comparison != EQUAL) return comparison; 
        
        return EQUAL;
      }
    };
  }
  
  private static Id getNewId(){
    fIdGenerator++;
    return Id.from(fIdGenerator.toString());
  }
  
  private Member getMemberWithId(Member aMember, Id aId)  {
    Member result = null;
    try {
      result = new Member(aId, aMember.getName(), aMember.getIsActive(), aMember.getDisposition().getId());
    }
    catch(ModelCtorException ex){
      throw new RuntimeException("Cannot build Member as expected");
    }
    return result;
  }
}
