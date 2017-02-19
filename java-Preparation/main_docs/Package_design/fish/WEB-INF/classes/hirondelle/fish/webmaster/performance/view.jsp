<%-- Show recent performance statistics. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Simple, effective tool for monitoring recent web application performance. 
  (Tool tips give a detailed description of each item.)
  Implemented as a Filter. See [link:http://www.javapractices.com/apps/web4j/javadoc/hirondelle/web4j/ui/filter/PerformanceMonitor.html PerformanceMonitor]
  for more information. See web.xml for Filter configuration details.

  Configuration parameters include numbers of days to store data, and the 
  length of each time period.

  This implementation stores performance statistics in memory only - _not in a database_.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
   [A Faire]
  </w:txt>
 </div>
</w:help>


<P>
<table class="report" title="Recent Performance Statistics" align="center"> 
 <caption>Recent Performance Statistics</caption>
 <tr>
  <th title="When this Performance Snapshot ended (or will end)">Period Ending</th>
  <th title="Number of Requests to Server (including images)">Hits</th>
  <th title="Average Response Time (secs)">Avg</th>
  <th title="Maximum Response Time (secs)">Max</th>
  <th title="Average in Bold, Maximum in Regular Font">Graph</th>
 </tr>
<jsp:useBean id="itemsForListing" scope="request" type="java.util.List" />
<% synchronized(itemsForListing) { %>
<w:alternatingRow> 
<c:forEach var="snapshot" items="${itemsForListing}">
 <tr class="row_highlight">
  <td title="When each Performance Snapshot ended (or will end)">
    <fmt:formatDate value="${snapshot.endTime}" pattern="MMM dd E k:mm" timeZone="GMT-4:00" />
  </td>
  <%-- Choose display characteristics of the graph according to max response time. --%>
  <c:set var="level">
   <c:choose>
    <c:when test="${snapshot.numRequests > 0}">performance-high</c:when>
    <c:otherwise>performance-low</c:otherwise>
   </c:choose>
  </c:set>
  <td align="right" class='${level}' title="Number of Requests to Server (including images)">
   <fmt:formatNumber pattern="#,##0">${snapshot.numRequests}</fmt:formatNumber>
  </td>
  <td align="right" title="Average Response Time (secs)">
   <fmt:formatNumber pattern="0.000">
    ${snapshot.avgResponseTime/1000}
   </fmt:formatNumber>
  </td>
   <%-- 
    The URL is presented here as a tool-tip. The URL is NOT presented as a link, 
    since, in general, clicking on such a link might edit the database.
   --%>
  <td align="right"  title='Maximum Response Time (secs). URL: ${snapshot.URLWithMaxResponseTime}' > 
   <fmt:formatNumber pattern="0.000">
    ${snapshot.maxResponseTime/1000}
   </fmt:formatNumber>
  </td>

  <%-- Choose display characteristics of the graph according to max response time. --%>
 <c:set var="level">
  <c:choose>
   <c:when test="${snapshot.maxResponseTime >= 10000}">
     performance-low
   </c:when>
   <c:when test="${snapshot.maxResponseTime >= 3000}">
     performance-medium
   </c:when>
   <c:otherwise>
    performance-high
   </c:otherwise>
  </c:choose>
 </c:set>

  <td class='${level}' title="Average in Bold, Maximum in Regular Font">
   <b><c:forEach var="idx" begin="0" end="${snapshot.avgResponseTime/1000}">0</c:forEach></b><c:forEach var="idx" begin="0" end="${snapshot.maxResponseTime/1000 - snapshot.avgResponseTime/1000}">0</c:forEach>
  </td>
 </tr>
</c:forEach>
</w:alternatingRow>
<% } %>
</table>
<P>

