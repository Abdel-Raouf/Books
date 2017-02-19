<%-- Allows any user to change their password. --%>
<%-- Unusual: this page does no prepopulation of any fields. This is a --%>
<%-- defensive measure, since it minimizes exposure of cleartext passwords. --%>
<%-- So, if an error occurs, then the user will need to re-input both items in full. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Change your password. If you have forgotten your password, you will 
  need to ask for a password *reset*. Once that is done, you may use this 
  screen to change your password to whatever you prefer.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:if test="${not empty itemForEdit}">
<c:url value="ChangePasswordAction.apply" var="baseURL"/> 
<form action='${baseURL}' method="post" class="user-input" >
 <table align="center">
  <tr>
   <td>
    <%-- The name is displayed here, but is NOT submitted as a request param. --%>
    <%-- That would be highly insecure, since one user could easily mimic another. --%>
    <%-- On the server, the user name is simply fetched from the existing session. --%>
    <label><w:txt>Name</w:txt></label>
   </td>
   <td>${itemForEdit}</td>
  </tr>
  <tr>
   <td><label><w:txt>Old Password</w:txt></label></td>
   <td><input name="Old Password" type="password" autocomplete="false"></td>
  </tr>
  <tr>
   <td><label><w:txt>New Password</w:txt></label></td>
   <td><input name="New Password" type="password" autocomplete="false"></td>
  </tr>
  <tr>
   <td><label><w:txt>Confirm New Password</w:txt></label></td>
   <td><input name="Confirm New Password" type="password" autocomplete="false"></td>
  </tr>
  <tr>
   <td align="center" colspan=2>
    <input type="submit" value="Change Password">
   </td>
  </tr>
 </table>
</form>
</c:if>