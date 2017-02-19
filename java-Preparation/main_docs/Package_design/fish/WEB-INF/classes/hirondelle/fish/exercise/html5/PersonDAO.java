package hirondelle.fish.exercise.html5;

import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/** 
 Data Access Object (DAO) for {@link Person} objects.
 This implementation is a stub, which only uses objects in memory, in static scope.
 No real persistence occurs. This class should synchronize on the shared data, but that 
 is left out.
*/
final class PersonDAO {

  Collection<Person> list() throws DAOException {
    return fPersons.values();
  }
   
  /** Return a single {@link Person} identified by its id.  */
  public Person fetch(Id aPersonId)  {
    return fPersons.get(aPersonId);
  }
   
  /** Add a new {@link Person} to the database. The name must be unique.*/
  void add(Person aPerson) throws ModelCtorException  {
    if (! fPersons.containsKey(aPerson.getId())){
      ++ID_COUNTER;
      Id id = Id.from(ID_COUNTER.toString());
      Person personWithId = new Person(
        id, aPerson.getName(), aPerson.getEmail(), aPerson.getWebsite(), aPerson.getWeight(), 
        aPerson.getPhone(), aPerson.getColor(), aPerson.getRating(), aPerson.getBorn()
      );
      fPersons.put(id, personWithId);
    }
  }
   
  /**  Update an existing {@link Person}. */
  void change(Person aPerson) throws DAOException, DuplicateException {
    if(fPersons.containsKey(aPerson.getId())){
      fPersons.put(aPerson.getId(), aPerson);
    }
  }
  
  /** Delete a single {@link Person}.  */
  void delete(Id aPersonId) throws DAOException {
    fPersons.remove(aPersonId);
  }
   
  // PRIVATE
  private static final Map<Id, Person> fPersons = new LinkedHashMap<Id, Person>();
  private static Integer ID_COUNTER = 0;
}
