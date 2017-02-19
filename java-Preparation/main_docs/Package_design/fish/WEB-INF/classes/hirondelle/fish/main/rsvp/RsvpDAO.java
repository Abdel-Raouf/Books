package hirondelle.fish.main.rsvp;

import java.util.*;
import java.util.logging.Logger;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.util.Util;
import hirondelle.fish.main.member.Member;
import hirondelle.web4j.database.Db;

/**
 Data Access Object for {@link Rsvp} objects.
*/
final class RsvpDAO {
  
  /**
   Return a {@link List} of {@link Rsvp} objects for for the next visit scheduled 
   in the future.
  */
  List<Rsvp> list(Id aNextVisitId) throws DAOException {
    /*
     The implementation of this method is overly complex. The reason is that the 
     target database version is old, and does not allow the desired result set
     to be obtained in a single query : no sub-selects allowed. Hence, some  
     manual 'wiring together' is needed. This would not be required if the 
     database version was more recent. 
    */
    List<Rsvp> result = getActiveMemberRsvps(aNextVisitId);
    addRsvpResponses(aNextVisitId, result);
    return Collections.unmodifiableList(result);
  }
  
  /** Fetch an {@link Rsvp} in preparation for editing it.  */
  Rsvp fetchForChange(Id aVisitId, Id aMemberId) throws DAOException {
    return Db.fetch(Rsvp.class, RsvpUpdate.RSVP_FETCH_FOR_CHANGE, aVisitId, aMemberId); 
  }
  
  /** Apply an update to an {@link Rsvp}. */
  void update(Rsvp aRsvp) throws DAOException {
    Object[] params = {aRsvp.getResponse(), aRsvp.getVisitId(), aRsvp.getMemberId()};
    Db.edit(RsvpUpdate.RSVP_CHANGE, params);
  }
  
  /** Add a new {@link Rsvp}. */
  void add(Rsvp aRsvp) throws DAOException {
    Object[] params = {aRsvp.getVisitId(), aRsvp.getMemberId(), aRsvp.getResponse()};
    Db.edit(RsvpAdd.RSVP_ADD, params);
  }
  
  /** Return the number of 'yes' responses for a given visit.  */
  Integer countNumYes(Id aVisitId) throws DAOException {
    return Db.fetchValue(Integer.class, RsvpShow.RSVP_FETCH_NUM_YES, aVisitId);
  }
  
  // PRIVATE //
  
  private static final Logger fLogger = Util.getLogger(RsvpDAO.class);  
  
  /**
   Return List of {@link Rsvp} objects for all active {@link Member}s.
   
   <P> Here, the response is left blank, and not yet populated.
  */
  private List<Rsvp> getActiveMemberRsvps(Id aNextVisitId) throws DAOException {
    List<Rsvp> result = new ArrayList<Rsvp>();
    List<Member> activeMembers = Db.list(Member.class, RsvpShow.RSVP_LIST_ACTIVE_MEMBERS);
    for(Member member : activeMembers){
      Rsvp rsvp = null;
      try  {
        rsvp = new Rsvp(aNextVisitId, member.getId(), member.getName(), null);
      }
      catch (ModelCtorException ex){
        fLogger.severe("Unexpected exception in RsvpDAO. Cannot construct Rsvp object.");
      }
      result.add(rsvp);
    }
    fLogger.fine("Active Members, no Rsvp responses : " + result);
    return result;
  }
  
  private void addRsvpResponses(Id aNextVisitId, List<Rsvp> aActiveMembers) throws DAOException {
    List<Rsvp> responses = Db.list(Rsvp.class, RsvpShow.RSVP_LIST_RESPONSES, aNextVisitId);
    for(Rsvp activeMember: aActiveMembers){
      for(Rsvp response: responses){
        if ( activeMember.getMemberId().equals(response.getMemberId())){
          activeMember.setResponse(response.getResponse());
        }
      }
    }
    fLogger.fine("Active Members, with Rsvp responses added: " + aActiveMembers);
  }
}
