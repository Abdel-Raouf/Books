<%-- Listing of Active Members, with any RSVP responses. --%>
<%-- RSVP responses are changed on another page. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Allows users to RSVP for the next future Visit. 
  No extra security, anyone can edit these items.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:if test="${empty nextVisit}">
 <p align="center"><w:txt>There is no scheduled future Visit.</w:txt></p>
</c:if>

<c:if test="${not empty nextVisit}">
<p align="center">
 <w:txt>What's Next</w:txt> : <b>${nextVisit.restaurant}</b><br>
 <c:set value="${nextVisit.lunchDate}" var="lunchDate"/>
 <w:txt>Date</w:txt> : <b><w:showDate name="lunchDate"  patternKey="format.next.lunch.date"/></b><br> 
 <w:txt>Num "Yes" Responses</w:txt> : <b>${numYesResponses}</b><br>
 ${nextVisit.message}
</p>
<w:txtFlow>
<table class="report" title="Active Members" align="center"> 
 <caption>Active Members</caption>
 <tr>
  <th>Yes</th>
  <th>No</th>
  <th>Name</th>
 </tr>
 </w:txtFlow>
 
<w:alternatingRow> 
<c:forEach var="rsvp" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <%-- Show Yes/No, according to any existing rsvp selections. --%>
  <w:txtFlow>
  <c:if test="${empty rsvp.response}">
   <td>-</td>
   <td>-</td>
  </c:if>
  <c:if test="${rsvp.response}">
   <td>Y</td>
   <td>-</td>
  </c:if>
  <c:if test="${rsvp.response == 'false'}">
   <td>-</td>
   <td>N</td>
  </c:if>
  </w:txtFlow>

  <%-- Link for either adding an RSVP response, or changing an existing one. --%>  
  <c:if test="${empty rsvp.response}">
   <td>
    <c:url value='RsvpAdd.show' var='addURL'>
     <c:param name='MemberId' value='${rsvp.memberId}' />
     <c:param name='VisitId' value='${nextVisit.id}' />
    </c:url>
    <a title='Add Rsvp' href='${w:ampersand(addURL)}'>${rsvp.memberName}</a>
   </td>
  </c:if>
  <c:if test="${rsvp.response == 'true' || rsvp.response == 'false'}">
   <td>
    <c:url value='RsvpUpdate.show' var='updateURL'>
     <c:param name='MemberId' value='${rsvp.memberId}' />
     <c:param name='VisitId' value='${nextVisit.id}' />
    </c:url>
    <a title="Change Rsvp" href="${w:ampersand(updateURL)}">${rsvp.memberName}</a>
   </td>
  </c:if>
 </tr>
</c:forEach>
</w:alternatingRow>
</table>
 
</c:if>


