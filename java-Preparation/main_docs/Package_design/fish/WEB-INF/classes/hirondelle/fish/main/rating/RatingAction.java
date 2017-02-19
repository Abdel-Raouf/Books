package hirondelle.fish.main.rating;

import hirondelle.fish.util.ReqParam;
import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;

import java.util.List;
import java.util.regex.Pattern;

/**
 Allow ratings to be edited for each {@link hirondelle.fish.main.resto.Resto}.
 
 @sql statements.sql
 @view view.jsp
*/
public final class RatingAction extends ActionTemplateListAndEdit {
  
  /** Constructor.  */
  public RatingAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
  }

  public static final SqlId RATING_LIST = new SqlId("RATING_LIST");
  public static final SqlId RATING_FETCH_FOR_CHANGE = new SqlId("RATING_FETCH_FOR_CHANGE");
  public static final SqlId RATING_CHANGE = new SqlId("RATING_CHANGE");
  
  private static final Pattern fRATING_REGEX = Pattern.compile("(0|1|2|3|4|5|6|7|8|9|10)");
  public static final RequestParameter RATING_ID = ReqParam.ID;
  public static final RequestParameter FISH_RATING = RequestParameter.withRegexCheck("FishRating", fRATING_REGEX);
  public static final RequestParameter CHIPS_RATING =  RequestParameter.withRegexCheck("ChipsRating", fRATING_REGEX);
  public static final RequestParameter PRICE_RATING =  RequestParameter.withRegexCheck("PriceRating", fRATING_REGEX);
  public static final RequestParameter LOCATION_RATING = RequestParameter.withRegexCheck("LocationRating", fRATING_REGEX);
  public static final RequestParameter SERVICE_RATING = RequestParameter.withRegexCheck("ServiceRating", fRATING_REGEX);
  public static final RequestParameter BEER_RATING = RequestParameter.withRegexCheck("BeerRating", fRATING_REGEX);
  
  /** List ratings for all restaurants.  */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, listAllRatings());
  }

  /** Fetch a {@link Rating} in preparation for editing it.   */
  protected void attemptFetchForChange() throws DAOException {
    Rating rating = fetchRating(getIdParam(RATING_ID));
    if ( rating != null ){
      addToRequest(ITEM_FOR_EDIT, rating);
    }
    else {
      addError("Cannot fetch the selected item. Likely deleted by another user.");
    }
  }
  
  /** Ensure user input can create a {@link Rating}.  */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fRating = builder.build(Rating.class, RATING_ID, null, FISH_RATING, CHIPS_RATING, PRICE_RATING, LOCATION_RATING, SERVICE_RATING, BEER_RATING, null);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }
  }

  /** Apply an edit to a {@link Rating}.  */
  protected void attemptChange() throws DAOException {
    int numEdits = change(fRating);
    if ( numEdits == 0 ){
      addError("Update failed. Has item been deleted by another user?");
    }
  }
  
  /**
   Not implemented in this case. The ratings are created at the same time as the 
   underlying Restaurant, with <tt>0</tt> default values for each rated item.
  */
  protected void attemptAdd() throws DAOException {
    //empty
  }
  
  /** Not implemented in this case.  */
  protected void attemptDelete() throws DAOException {
    //empty
  }
  
  // PRIVATE //
  private Rating fRating;
  
  private static final ResponsePage FORWARD = new ResponsePage(
    "Ratings", "view.jsp", RatingAction.class
  );
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("RatingAction.list");
  
  /** Return a <tt>List</tt> of all {@link Rating} objects, for all restaurants.  */
  private List<Rating> listAllRatings() throws DAOException {
    return Db.list(Rating.class, RATING_LIST);
  }
  
  /** Return a single {@link Rating} identified by its id.  */
  private Rating fetchRating(Id aRatingId) throws DAOException {
    return Db.fetch(Rating.class, RATING_FETCH_FOR_CHANGE, aRatingId);
  }
  
  /** Update an existing {@link Rating}. */
  private int change(Rating aRating) throws DAOException {
    Object[] params = { 
      aRating.getFishRating(), aRating.getChipsRating(), aRating.getPriceRating(), 
      aRating.getLocationRating(), aRating.getServiceRating(), 
      aRating.getBeerRating(), aRating.getId() 
    };
    return Db.edit(RATING_CHANGE, params);
  }
}
