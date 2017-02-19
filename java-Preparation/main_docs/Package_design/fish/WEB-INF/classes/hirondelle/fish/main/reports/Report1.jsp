<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Various reports of interest.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<P>
<table class="report-panel" align="center">
 <tr>
 <tags:reportLinks reportIdx='1'/>
 </tr> 
</table>

<w:txtFlow escapeChars="false">
<table class="report" title="Visits Per Restaurant" align="center"> 
 <tr><th colspan="4">How many Visits for each Restaurant?</th></tr>
 <tr>
  <c:set value="Report1Action.show" var="baseURI"/>
  <th valign="middle">
   Restaurant <tags:sortLinks baseURI="${baseURI}" column="1"/>
  </th>
  <th>
   Visits <tags:sortLinks baseURI="${baseURI}" column="2"/>
  </th>
  <th>
   Graph
  </th>
 </tr>
</w:txtFlow>

<w:alternatingRow>
<c:forEach var="row" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td>${row['Name']}</td>  
  <td>${row['Visits']}</td>    
  <td><img src="../../images/green.gif" height="10" width="${10 * row['Visits'].rawString}" alt='graph'></td> 
 </tr>
</c:forEach>
</w:alternatingRow>
</table>
