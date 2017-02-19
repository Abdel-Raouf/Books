package hirondelle.fish.main.discussion;

import hirondelle.web4j.BuildImpl;
import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 List comments, and add new ones.
 
 <P>Comments are listed, along with a paging mechanism.
 
 @sql statements.sql
 @view view.jsp
*/
public final class CommentAction extends ActionTemplateShowAndApply {
  
  public static final SqlId FETCH_RECENT_COMMENTS =  new SqlId("FETCH_RECENT_COMMENTS");
  public static final SqlId ADD_COMMENT =  new SqlId("ADD_COMMENT");
  
  /** Constructor. */
  public CommentAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }
  
  public static final RequestParameter COMMENT_BODY = RequestParameter.withLengthCheck(
    "Comment Body"
   );

  /** Used for the paging mechanism. */
  public static final RequestParameter PAGE_SIZE = RequestParameter.withRegexCheck(
    "PageSize", Pattern.compile("(\\d){1,4}")
   );
  /** Used for the paging mechanism. */
  public static final RequestParameter PAGE_INDEX = RequestParameter.withRegexCheck(
    "PageIndex", Pattern.compile("(\\d){1,4}")
  );
  
  /** Show the listing of comments, and a form for adding new messages. */
  protected void show() throws AppException {
    addToRequest(ITEMS_FOR_LISTING, fetchRecentComments());
  }
  
  /** Ensure user input can build a new {@link Comment}.  */
  protected void validateUserInput() throws AppException {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      /*
        This is an example of using a time which, for testing purposes,
        can be made independent of the true system time. The value of 
        the 'now' variable depends on the implementation of TimeSource.
       */
      long now = BuildImpl.forTimeSource().currentTimeMillis();
      fComment = builder.build(
        Comment.class, getLoggedInUserName(), COMMENT_BODY, new Date(now)
      );
    }
    catch (ModelCtorException ex){
      addError(ex);
    }
  }
  
  /** Add a new {@link Comment} to the database. */
  protected void apply() throws AppException {
    //no possibility of a duplicate error.
    addNew(fComment);
  }
  
  // PRIVATE //
  private Comment fComment;
  
  private static final ResponsePage FORWARD = new ResponsePage(
    "Discussion", "view.jsp", CommentAction.class
  );
  private static final ResponsePage REDIRECT =  new ResponsePage(
    "CommentAction.show?PageIndex=1&PageSize=10"
  );
  
  /*
   Here, the DAO methods are not in a separate class, but simply regular methods of 
   this Action. This reasonable since the methods are short, and they do not make 
   this Action class significantly more complex. 
  */
  
  /**
   Return an immutable {@link List} of recent {@link Comment} objects.
  
   <P>The definition of what constitutes "recent" is left deliberately vague, to 
   allow various versions of "recent" - last 5 messages, messages entered over 
   the last N days, etc.
  */
  private List<Comment> fetchRecentComments() throws DAOException {
    return Db.list(Comment.class, FETCH_RECENT_COMMENTS);
  }

  /**
   Add <tt>aNewComment</tt> to the database. No duplicates are possible.
  
   @param aNewComment to be added to the datastore.
  */
  private void addNew(Comment aNewComment) throws DAOException {
    Object[] params = { 
      aNewComment.getUserName(), aNewComment.getBody(), aNewComment.getDate() 
    };
    Db.edit(ADD_COMMENT, params);
  }
}
