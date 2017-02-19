<%-- List-and-edit form for Members. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Edit Member data. Name and Disposition are mandatory.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:set value='MemberAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

<w:populate using="itemForEdit">
 <form action='${formTarget}' method="post" class="user-input" name='giveMeFocus'>
 <w:txtFlow> 
  <input name="Id" type="hidden">
  <table align="center">
   <tr>
    <td><label>Name</label> *</td>
    <td><input name="Name" type="text"></td>
   </tr>
   <tr>
    <td><label>Is Active?</label></td>
    <td><input name="IsActive" type="checkbox" value="true"></td>
   </tr>
   <tr>
    <td><label>Disposition</label> *</td>
    <td>
     <select name="Disposition">
      <option> </option>
      <c:forEach var="item" items="${dispositions}">
        <option value="${item.id}">${item.text.rawString}</option>
      </c:forEach>
     </select>
    </td>
   </tr>
 </w:txtFlow> 
   <tr>
    <td align="center" colspan=2>
     <input type="submit" value="add.edit.button">
    </td>
   </tr>
  </table>
</form>
</w:populate>

<%-- This style allows for deleting only one item at a time. --%>
<%--
<P>
<w:txtFlow>
 <table class="report" title="Members" align="center"> 
  <caption>Members (${numActiveMembers} active)</caption>
  <tr>
   <th title="Line Number">#</th>
   <th>Name</th>
   <th>Is Active?</th>
   <th>Disposition</th>
  </tr>
</w:txtFlow>

<w:alternatingRow> 
<c:forEach var="member" items="${itemsForListing}" varStatus="index">
  <tr class="row_highlight">
   <td title="Line Number">${index.count}</td>
   <td>${member.name}</td>
   <td align="center">
    <c:if test="${member.isActive}">
     <img src='../../images/yes.gif' class='no-margin' alt='Yes'>
    </c:if>
    <c:if test="${not member.isActive}">
     <img src='../../images/no.gif' class='no-margin' alt='No'>
    </c:if>
   </td>
   <td>${member.disposition}</td>
   <tags:editLinksFineGrained baseURL='${baseURL}' id='${member.id}'/>
  </tr>
</c:forEach>
</w:alternatingRow>
</table>
--%>


<%-- This style allows for deleting more than one item at a time. --%>
<P>
<w:txtFlow>
<c:url value='${baseURL}.delete' var='deleteURL' />
<form action='${w:ampersand(deleteURL)}' method='POST'> 
 <table class="report" title="Members" align="center"> 
  <caption>Members (${numActiveMembers} active)</caption>
  <tr>
   <th title="Line Number">#</th>
   <th>Name</th>
   <th>Is Active?</th>
   <th>Disposition</th>
   <th>Edit</th>
   <th>
     <input type='submit' value='Delete'>
   </th>
  </tr>
</w:txtFlow>

<w:alternatingRow> 
<c:forEach var="member" items="${itemsForListing}" varStatus="index">
  <tr class="row_highlight">
   <td title="Line Number">${index.count}</td>
   
   <td>${member.name}</td>
   
   <td align="center">
    <c:if test="${member.isActive}">
     <img src='../../images/yes.gif' class='no-margin' alt='Yes'>
    </c:if>
    <c:if test="${not member.isActive}">
     <img src='../../images/no.gif' class='no-margin' alt='No'>
    </c:if>
   </td>
   
   <td>${member.disposition}</td>
   
  <td align="center">
    <c:url value='${baseURL}.fetchForChange' var='editURL' >
    <c:param name='Id' value='${member.id}' />
   </c:url>
   <a href='${w:ampersand(editURL)}'><w:txt>edit</w:txt></a>
  </td>
  
  <td align="center"  title='Check to delete'>
   <c:url value='${baseURL}.delete' var='deleteURL' >
    <c:param name='Id' value='${id}' />
   </c:url>
   <input type="checkbox" value='${member.id}' name='Id'>
  </td>
  
  </tr>
</c:forEach>
</w:alternatingRow>
</table>
</form>