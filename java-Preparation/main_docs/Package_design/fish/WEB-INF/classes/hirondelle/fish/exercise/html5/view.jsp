<%-- List-and-edit form for testing HTML5 controls. --%>

<c:set value='PersonAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

<w:populate using="itemForEdit">
 <form action='${formTarget}' method="post" class="user-input" name='giveMeFocus'>
  <input name="Id" type="hidden">
  <table align="center">
   <tr>
    <td><label>Name</label> *</td>
    <td><input name="Name" type="search" required='true'></td>
   </tr>
   <tr>
    <td><label>Email</label></td>
    <td><input name="Email" type="email"></td>
   </tr>
   <tr>
    <td><label>Web site</label></td>
    <td><input name="Website" type="url"></td>
   </tr>
   <tr>
    <td><label>Weight</label></td>
    <td><input name="Weight" type="number"></td>
   </tr>
   <tr>
    <td><label>Phone</label></td>
    <td><input name="Phone" type="tel"></td>
   </tr>

   <!-- Color and Range are not nullable, according to the HTML5 draft spec. -->   
   <tr>
    <td><label>Color</label></td>
    <td><input name="Color" type="color"></td>
   </tr>
   <tr>
    <td><label>Rating</label></td>
    <td><input name="Rating" type="range" min='1' max='4'></td>
   </tr>
   
   <tr>
    <td align="center" colspan=2>
     <input type="submit" value="Add/Edit">
    </td>
   </tr>
  </table>
</form>
</w:populate>

<%-- This style allows for deleting only one item at a time. --%>
<P>
 <table class="report" title="People" align="center"> 
  <caption>People</caption>
  <tr>
   <th>Name</th>
   <th>Email</th>
   <th>Website</th>
   <th>Weight</th>
   <th>Phone</th>
   <th>Color</th>
   <th>Rating</th>
   <th>Born</th>
  </tr>

<w:alternatingRow> 
<c:forEach var="person" items="${itemsForListing}" varStatus="index">
  <tr class="row_highlight">
   <td>${person.name}</td>
   <td>${person.email}</td>
   <td>${person.website}</td>
   <td>${person.weight}</td>
   <td>${person.phone}</td>
   <td  style='background-color:${person.color};'></td>
   <td>${person.rating}</td>
   <td>${person.born}</td>
   <tags:editLinksFineGrained baseURL='${baseURL}' id='${person.id}'/>
  </tr>
</c:forEach>
</w:alternatingRow>
</table>


