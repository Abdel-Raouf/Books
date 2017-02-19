package hirondelle.fish.exercise.html5;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

/** Edit Persons. */
public final class PersonAction extends ActionTemplateListAndEdit {

  /** Constructor.  */
  public PersonAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }
  
  public static final RequestParameter PERSON_ID = ReqParam.ID;
  public static final RequestParameter NAME = ReqParam.NAME;
  public static final RequestParameter EMAIL = RequestParameter.withLengthCheck("Email");
  public static final RequestParameter WEBSITE = RequestParameter.withLengthCheck("Website");
  public static final RequestParameter WEIGHT = RequestParameter.withLengthCheck("Weight");
  public static final RequestParameter PHONE = RequestParameter.withLengthCheck("Phone");
  public static final RequestParameter COLOR = RequestParameter.withLengthCheck("Color");
  public static final RequestParameter RATING = RequestParameter.withLengthCheck("Rating");
  public static final RequestParameter BORN = RequestParameter.withLengthCheck("Born");
  
  /** List all Persons, sorted by name. */
  protected void doList() throws DAOException {
    fLogger.fine("Showing list of all persons.");
    addToRequest(ITEMS_FOR_LISTING, fPersonDAO.list());
  }

  /** Ensure user input can build a {@link Person}.  */
  protected void validateUserInput() {
    fLogger.fine("Validating user input.");
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fPerson = builder.build(Person.class, PERSON_ID, NAME, EMAIL, WEBSITE, WEIGHT, PHONE, COLOR, RATING, BORN);
      fLogger.fine("New Person: " + fPerson);
    }
    catch (ModelCtorException ex){
      fLogger.fine("User input cannot create a Person.");
      addError(ex);
    }    
  }
  
  /** Add a new {@link Person}.  */
  protected void attemptAdd() throws DAOException {
    fLogger.fine("Adding valid Person to database: " + fPerson);
    try {
      fPersonDAO.add(fPerson);
    }
    catch (ModelCtorException ex){
      addError("Can't add new Person.");
    }
    addMessage("_1_ added successfully.", fPerson.getName());
  }
  
  /** Fetch an existing {@link Person} in order to edit it.  */
  protected void attemptFetchForChange() throws DAOException {
    Person person = fPersonDAO.fetch(getIdParam(PERSON_ID));
    if( person == null ){
      addError("Item no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, person);
    }
  }
  
  /** Apply an edit to an existing {@link Person}.  */
  protected void attemptChange() throws DAOException {
      fPersonDAO.change(fPerson);
      addMessage("_1_ changed successfully.", fPerson.getName());
  }
  
  /** 
   Delete existing {@link Person}s.  
   Deletion will fail if some other item is linked to a <tt>Person</tt>. 
  */
  protected void attemptDelete() throws DAOException {
    fLogger.fine("Attempting to delete 1 or more persons.");
    fPersonDAO.delete(getIdParam(PERSON_ID));
  }
  
  // PRIVATE //
  private Person fPerson;
  private PersonDAO fPersonDAO = new PersonDAO();
  private static final ResponsePage FORWARD = new ResponsePage("Persons", "view.jsp", PersonAction.class);
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("PersonAction.list");
  private static final Logger fLogger = Util.getLogger(PersonAction.class);
}