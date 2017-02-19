<P>
<table class="report-panel" align="center">
 <tr>
  <tags:reportLinks reportIdx='2'/>
 </tr> 
</table>

<w:txtFlow>
<table class="report" title="Visits Per Member" align="center"> 
 <tr>
  <th colspan="4">How many Visits for each Member?</th>
 </tr>
 <tr>
  <c:set value="Report2Action.show" var="baseURI"/>
  <th>Member <tags:sortLinks baseURI="${baseURI}" column="1"/></th>
  <th>Visits <tags:sortLinks baseURI="${baseURI}" column="2"/></th>
  <th>Graph</th>
 </tr>
</w:txtFlow>

<w:alternatingRow> 
  <c:forEach var="row" items="${itemsForListing}" varStatus="index">
   <tr class="row_highlight">
    <td>${row['Name']}</td>  
    <td>${row['Visits']}</td>  
    <td><img src="../../images/green.gif" height="10" width="${10 * row["Visits"].rawString}" alt='Graph'></td>
   </tr>
  </c:forEach>
</w:alternatingRow>
</table>


