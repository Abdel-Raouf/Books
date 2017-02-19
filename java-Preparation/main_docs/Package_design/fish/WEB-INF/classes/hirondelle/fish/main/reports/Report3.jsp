<P>
<table class="report-panel" align="center">
 <tr>
   <tags:reportLinks reportIdx='3'/>
 </tr> 
</table>

<w:txtFlow>
<table class="report" title="Number of Members Attending Recent Visits" align="center"> 
 <tr>
  <th colspan="4">How many Members came to recent Visits?</th>
 </tr>
 <tr>
  <c:set value="Report3Action.show" var="baseURI"/>
  <th>Date <tags:sortLinks baseURI="${baseURI}" column="1"/></th>
  <th>Members <tags:sortLinks baseURI="${baseURI}" column="2"/></th>
  <th>Graph</th>
 </tr>
</w:txtFlow>
<w:alternatingRow> 

<c:forEach var="row" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td>
   <c:set value="${row['Date']}" var="lunchDate"/>
   <w:showDate name="lunchDate" pattern='yyyy-MM-dd'/>    
   </td>  
  <td>${row['Members']}</td>  
  <td>
   <c:set var="endpoint">${row["Members"]}</c:set>
   <c:forEach var="item" begin="1" end="${endpoint}">&bull;</c:forEach>
  </td>  
 </tr>
</c:forEach>
</w:alternatingRow>
</table>

