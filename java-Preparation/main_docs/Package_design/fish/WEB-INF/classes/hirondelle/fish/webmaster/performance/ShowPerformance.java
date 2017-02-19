package hirondelle.fish.webmaster.performance;

import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.webmaster.PerformanceMonitor;
import hirondelle.web4j.webmaster.PerformanceSnapshot;

import java.util.List;
import java.util.logging.Logger;

/**
 Display response time statistics collected by a {@link PerformanceMonitor}.
 
 @view view.jsp
*/
public final class ShowPerformance extends ActionImpl {

  /** Constructor. */
  public ShowPerformance(RequestParser aRequestParser) {
    super(FORWARD, aRequestParser);
  }

  /**
   Call {@link PerformanceMonitor#getPerformanceHistory()}, and place that <tt>List</tt>
   of {@link hirondelle.web4j.webmaster.PerformanceSnapshot} objects in request scope for
   rendering.
   
   <P>Relies on a servlet filter being configured to collect these statistics. See <tt>web.xml</tt>.
  */
  public ResponsePage execute() {
    List<PerformanceSnapshot> snapshots = PerformanceMonitor.getPerformanceHistory();
    fLogger.info("Showing " + snapshots.size() + " Performance Statistics.");
    addToRequest(ITEMS_FOR_LISTING, snapshots);
    return getResponsePage();
  }

  // PRIVATE //
  private static final ResponsePage FORWARD = new ResponsePage(
    "Performance Monitor", "view.jsp", ShowPerformance.class
  );

  private static final Logger fLogger = Util.getLogger(ShowPerformance.class);
}
