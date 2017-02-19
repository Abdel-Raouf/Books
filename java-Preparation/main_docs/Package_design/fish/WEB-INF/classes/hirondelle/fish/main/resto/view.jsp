<%-- List-and-edit form for Restaurants. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Edit Restaurant data. Name is mandatory. Price is in range 0.00..100.00
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:set value='RestoAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

<w:populate using="itemForEdit"> 
 <w:txtFlow>
  <form action='${formTarget}' method="post" class="user-input" name='giveMeFocus'> 
   <input name="Id" type="hidden">
   <table align="center">
    <tr>
     <td><label>Name</label> *</td>
     <td><input name="Name" type="text"></td>
    </tr>
    <tr>
     <td><label>Location</label></td>
     <td><input name="Location" type="text"></td>
    </tr>
    <tr>
     <td><label>Price</label></td>
     <td><input name="Price" type="text"></td>
    </tr>
    <tr>
     <td><label>Comment</label></td>
     <td><input name="Comment" type="text"></td>
    </tr>
  </w:txtFlow>
    <tr>
     <td align="center" colspan=2>
      <input type='submit' value="add.edit.button">
     </td>
    </tr>
   </table>
  </form>
</w:populate>

<P>
<%-- Listing of all restaurants. --%>
<table class="report" title="Restaurants" align="center"> 
 <w:txtFlow>
 <caption>Restaurants</caption>
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
  <tags:editLinksFineGrained baseURL='${baseURL}' id='${restaurant.id}'/>
 </tr>
</c:forEach>
</w:alternatingRow>
</table>

