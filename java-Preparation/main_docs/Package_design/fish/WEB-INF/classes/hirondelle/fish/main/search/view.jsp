<%-- Search for Restaurants. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Search for Restaurants according to various criteria. Name is required.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:url value="RestoSearchAction.search" var="searchURL"/> 
<w:populate> 
 <w:txtFlow>
  <form action='${searchURL}' method="GET" class="user-input" name='giveMeFocus'>
   <table align="center">
    <tr>
     <td><label>Resto Name Starts With</label> *</td>
     <td><input name="Starts With" type="text" size='25'></td>
    </tr>
   <tr>
    <td><label>Price Between </label></td>
    <td><input name="Minimum Price" type="text" size='7' title='Minimum Price'> and <input name="Maximum Price" type='text' size='7' title='Minimum Price'></td>
   </tr>
   <tr>
    <td><label>Order By</label></td>
    <td>
     <select name="Order By">
      <option value='Name'>Name</option>
      <option value='Price'>Price</option>
     </select>
    <input name="Reverse Sort" type="checkbox" value="true"> Descending
    </td>
   </tr>
   <tr>
    <td align="center" colspan=2>
     <input type='submit' value="Search For Restaurant">
    </td>
   </tr>
   </table>
  </form>
 </w:txtFlow>
</w:populate>

<c:if test="${not empty itemsForListing}">
<P>
<%-- Listing of restaurants satisfying above criteria. --%>
<table class="report" title="Restaurants" align="center"> 
 <w:txtFlow>
 <caption>Restaurant Search</caption>
 <tr>
  <th title="Line Number">#</th>
  <th>Name</th>
  <th>Location</th>
  <th>Price</th>
  <th>Comment</th>
 </tr>
 </w:txtFlow>
<w:alternatingRow> 
<c:forEach var="restaurant" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td title="Line Number">${index.count}</td>
  <td>${restaurant.name}</td>
  <td align="center">${restaurant.location}</td>
  <td>${restaurant.price}</td>
  <td align="center">${restaurant.comment}</td>
 </tr>
</c:forEach>
</table>
</w:alternatingRow>
</c:if>
