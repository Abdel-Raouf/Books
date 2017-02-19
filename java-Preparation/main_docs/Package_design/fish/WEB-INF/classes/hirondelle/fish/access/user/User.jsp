<%-- List-and-edit form for Users. Here, only list, add and delete operations occur. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Add and Delete users. When you add a new user, be sure to enter their Roles as well, using the 
  Roles screen.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:set value='UserAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

 <w:txtFlow>
  <form action='${formTarget}' method="post" class="user-input" >
   <table align="center">
   <w:populate>
    <tr>
     <td><label>Name</label> *</td>
     <td><input name="Name" type="text"></td>
    </tr>
   </w:populate>
    <tr>
     <td align="center" colspan=2>
      <input type="hidden" name="Operation" value="Add">
      <input type='submit' value="add.edit.button">
     </td>
    </tr>
   </table>
  </form>
 </w:txtFlow>

<P>
<%-- Listing of all users. --%>
<table class="report" title="Users" align="center"> 
 <w:txtFlow>
  <caption>Users</caption>
  <tr>
   <th title="Line Number">#</th>
   <th>Name</th>
   <th></th>
  </tr>
  </w:txtFlow>
  <w:alternatingRow> 
   <c:forEach var="item" items="${itemsForListing}" varStatus="index">
    <tr class="row_highlight">
     <td title="Line Number">${index.count}</td>
     <td>${item.name}</td>
     <td align="center">
      <c:url value="${baseURL}.delete" var="deleteUserURL">
       <c:param name="Name" value="${item.name.rawString}"/>
      </c:url>
     <form action='${w:ampersand(deleteUserURL)}' method='POST'><input type="submit" value='Delete'></form> 
     </td>
    </tr>
   </c:forEach>
  </w:alternatingRow>
</table>

