<%-- List-and-edit form for User Roles. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Edit the Roles attached to each User.
  by default, a User has no Roles at all. When a new User is added, you must use this 
  screen to assign them one or more Roles.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:set value='RoleAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

<c:if test="${not empty itemForEdit}">
 <w:populate using="itemForEdit">
  <form action='${formTarget}' method="post" class="user-input" name='giveMeFocus'>
   <table align="center">
    <tr>
     <td><label><w:txt>Name</w:txt></label></td>
     <td>${itemForEdit.userName}</td>
    </tr>
    <tr>
     <td><label><w:txt>Roles</w:txt></label></td>
     <td>
      <input type="checkbox" name="Roles" value="user-general"> <w:txt>User (General)</w:txt><br>
      <input type="checkbox" name="Roles" value="user-president"> <w:txt>User (President)</w:txt><br>
      <input type="checkbox" name="Roles" value="webmaster"> <w:txt>Webmaster</w:txt><br>
      <input type="checkbox" name="Roles" value="translator"> <w:txt>Translator</w:txt><br>
      <input type="checkbox" name="Roles" value="access-control"> <w:txt>Access Control</w:txt>
     </td>
    </tr>
   <input type="hidden" name="UserName">
    <tr>
     <td align="center" colspan=2>
      <input type="submit" value="Update">
     </td>
    </tr>
   </table>
  </form>
 </w:populate>
</c:if>

<P>
<%-- Listing of all Users and their current Roles. --%>
<w:txtFlow>
 <table class="report" title="User Roles" align="center"> 
  <caption>User Roles</caption>
  <tr>
   <th title="Line Number">#</th>
   <th>Name</th>
   <th>Roles</th>
  </tr>
</w:txtFlow>
<c:if test="${not empty itemsForListing}">
 <w:alternatingRow> 
  <c:forEach var="userRoles" items="${itemsForListing}" varStatus="index">
   <tr class="row_highlight">
    <td title="Line Number">${index.count}</td>
    <td>${userRoles.userName}</td>
    <td>
     <%-- These can be translated as 'coder keys', if desired. --%>
     <c:forEach var="role" items="${userRoles.roles}">
      <tt>${role}</tt>
     </c:forEach>
    </td>
    <td title="Update" align="center">
     <c:url value='${baseURL}.fetchForChange'  var='editURL'>
      <c:param name='UserName' value='${userRoles.userName.rawString}' /> 
     </c:url>
      <a href="${w:ampersand(editURL)}"><w:txt>edit</w:txt></a> 
    </td>
   </tr>
  </c:forEach>
 </w:alternatingRow> 
 </table>
</c:if>
