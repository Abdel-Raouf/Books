<P>
<P>
<table class="report-panel" align="center">
 <tr>
  <tags:reportLinks reportIdx='4'/>
 </tr> 
</table>

<w:txtFlow>
<table class="report" title="Total Attendance Per Year" align="center"> 
 <caption>(Total : ${totalAttendanceAllYears})</caption>
 <tr>
  <th colspan="4">What was the total attendance per year?</th>
 </tr>
 <tr>
  <c:set value="Report4Action.show" var="baseURI"/>
  <th>Year <tags:sortLinks baseURI="${baseURI}" column="1"/></th>
  <th>Attendance <tags:sortLinks baseURI="${baseURI}" column="2"/></th>
 </tr>
</w:txtFlow>
<w:alternatingRow> 
<c:forEach var="row" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td>${row['Year']}</td>  
  <td>${row['Attendance']}</td>  
 </tr>
</c:forEach>
</w:alternatingRow>
</table>

