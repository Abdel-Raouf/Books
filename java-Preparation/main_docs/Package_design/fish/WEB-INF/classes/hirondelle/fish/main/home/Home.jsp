<%-- Home page for the application --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  *Fish and Chips Club*, an example application for WEB4J.
  
  In the menu, the current page is highlighted in yellow, while the current focus is also indicated.
  If your browser supports the _:focus_ pseudo-class (Firefox), the focus will be shown in blue (cyan, actually).
  If not (Internet Explorer 6), then the focus is shown using an appearance determined by the browser.
  Hit TAB, Shift+TAB to move the focus forward and backward through the page.
  Sometimes the initial focus is on the first menu link, sometimes it is on the form.
  
  The Log Off item is a button, not a link. So are various DELETE operations. This is because having them as _links_ is a security problem: 
  any actions that have side effects on the server should be POSTed to the server using a form.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:if test="${not empty nextVisit}">
 <c:set value="${nextVisit.lunchDate}" var="lunchDate"/>
 <p align="center">
  <w:txt>What's Next</w:txt> : <b>${nextVisit.restaurant}</b><br>
  <c:url value='../rsvp/RsvpShow.do' var='rsvpURL'/>
  <w:txt>Date</w:txt> : <w:showDate name="lunchDate" pattern="MMM dd, yyyy" /> - <a href="${rsvpURL}">RSVP</a><br> 
  ${nextVisit.message}<br>
 </p>
</c:if>

<p align="center">
<img src="../../images/FishAndChips.jpg" alt="Fish And Chips">
</P>

