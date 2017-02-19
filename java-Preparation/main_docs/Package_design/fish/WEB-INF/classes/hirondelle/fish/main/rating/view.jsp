<%-- List-and-edit form for Restaurant Ratings. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Ratings of each Restaurant according to a number of fixed criteria, in the range 1..10
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:if test="${not empty itemForEdit || Operation == 'Change'}">
  <form action='RatingAction.change' method="post" class="user-input" name='giveMeFocus'>
  <w:populate using="itemForEdit">
   <input name="Id" type="hidden">
   <table align="center">
   <tr>
    <td><label><w:txt>Restaurant</w:txt></label></td>
    <td>${itemForEdit.restaurant}</td>
   </tr>

   <tr>
    <td><label><w:txt>Fish</w:txt></label></td>
    <td>
     <select name="FishRating">
      <option> </option>
       <c:forEach var="value" items="${ratings}">
         <option title='${value}'>${value}</option>
       </c:forEach>
     </select>
    </td>
   </tr>

   <tr>
    <td><label><w:txt>Chips</w:txt></label></td>
    <td>
     <select name="ChipsRating">
      <option> </option>
       <c:forEach var="value" items="${ratings}">
         <option title='${value}'>${value}</option>
       </c:forEach>
     </select>
    </td>
   </tr>

   <tr>
    <td><label><w:txt>Price</w:txt></label></td>
    <td>
     <select name="PriceRating">
      <option> </option>
       <c:forEach var="value" items="${ratings}">
         <option title='${value}'>${value}</option>
       </c:forEach>
     </select>
    </td>
   </tr>

   <tr>
    <td><label><w:txt>Location</w:txt></label></td>
    <td>
     <select name="LocationRating">
      <option></option>
       <c:forEach var="value" items="${ratings}">
         <option title='${value}'>${value}</option>
       </c:forEach>
     </select>
    </td>
   </tr>

   <tr>
    <td><label><w:txt>Service</w:txt></label></td>
    <td>
     <select name="ServiceRating">
      <option></option>
       <c:forEach var="value" items="${ratings}">
         <option title='${value}'>${value}</option>
       </c:forEach>
     </select>
    </td>
   </tr>

   <tr>
    <td><label><w:txt>Beer</w:txt></label></td>
    <td>
     <select name="BeerRating">
      <option></option>
       <c:forEach var="value" items="${ratings}">
         <option title='${value}'>${value}</option>
       </c:forEach>
     </select>
    </td>
   </tr>

   <tr>
    <td align="center" colspan=2>
     <input type="submit" value="Update">
    </td>
   </tr>
  </table>
  </w:populate>
  <input type="hidden" name="Operation" value="Change">
  </form>
</c:if>

<P>
<%-- Listing of all restaurant ratings. --%>
<w:txtFlow>
<table class="report" title="Restaurant Ratings" align="center"> 
 <caption>Ratings</caption>
 <tr>
  <th title="Line Number">#</th>
  <th>Restaurant</th>
  <th title="Graph">Graph</th>
  <th title="Overall Rating for restaurant">Overall</th>
  <th title="Weight: 3">Fish</th>
  <th title="Weight: 3">Chips</th>
  <th title="Weight: 3">Price</th>
  <th title="Weight: 2">Location</th>
  <th title="Weight: 2">Service</th>
  <th title="Weight: 1">Beer</th>
  <th title="Update"></th>
 </tr>
</w:txtFlow>

<c:if test="${not empty itemsForListing}">
 <w:alternatingRow> 
 <c:forEach var="rating" items="${itemsForListing}" varStatus="index">
  <tr class="row_highlight">
   <td title="Line Number">${index.count}</td>
   <td>${rating.restaurant}</td>
   <td><img src="../../images/green.gif" height="10" width="${10 * rating.overallRating}" alt="Graph"></td>
   <td>${rating.overallRating}</td>
   <td>${rating.fishRating}</td>
   <td>${rating.chipsRating}</td>
   <td>${rating.priceRating}</td>
   <td>${rating.locationRating}</td>
   <td>${rating.serviceRating}</td>
   <td>${rating.beerRating}</td>
   <td title="Update" align="center">
    <c:url value='RatingAction.fetchForChange' var='editURL' >
     <c:param name='Id' value='${rating.id}' />
    </c:url>
    <a href="${w:ampersand(editURL)}"><w:txt>edit</w:txt></a>
   </td>
  </tr>
 </c:forEach>
 </w:alternatingRow>
</table>
</c:if>
