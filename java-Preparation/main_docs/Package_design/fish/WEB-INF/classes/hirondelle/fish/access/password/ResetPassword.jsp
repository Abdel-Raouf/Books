<%-- List-and-edit form for resetting passwords. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Reset the user's password to a fixed, standard value.
  The value is inconveniently long, in order to encourage the user to change it.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:url value="ResetPasswordAction.apply" var="baseURL"/> 
<w:populate>
 <form action='${baseURL}' method="post" class="user-input" >
  <table align="center">
   <tr>
    <td><label><w:txt>Name</w:txt></label></td>
    <td>
     <select name="UserName">
      <option> </option>
      <c:forEach var="user" items="${itemsForListing}"> 
       <option>${user.name}</option>
      </c:forEach>
     </select>
    </td>
   </tr>
   <tr>
    <td align="center" colspan=2>
     <input type="submit" value="Reset Password">
    </td>
    </tr>
  </table>
 </form>
</w:populate>
 