package hirondelle.fish.util; 

import hirondelle.web4j.request.RequestParameter;
   
/** 
  Commonly used {@link RequestParameter}s that are used in more than one package.
*/
public final class ReqParam {

  /** Parameter called <tt>'Name'</tt>, with a length check. */
  public static final RequestParameter NAME = RequestParameter.withLengthCheck("Name");
  /** Parameter called <tt>'Id'</tt>, with a length check. */
  public static final RequestParameter ID = RequestParameter.withLengthCheck("Id");

  /** Control sorting. See the <tt>sortLinks.tag</tt> file. */
  public static final RequestParameter SORT_ON = RequestParameter.withRegexCheck("SortOn", "(1|2|3|4|5|6|7|8|9|10)");
  /** Control sorting. See the <tt>sortLinks.tag</tt> file. */
  public static final RequestParameter ORDER = RequestParameter.withRegexCheck("Order", "(ASC|DESC)");
  
}
