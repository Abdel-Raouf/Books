<%-- Change user preferences. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Change your preferences for this application.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:url value="PreferencesAction.apply" var="baseURL"/> 
<form action='${baseURL}' method="post" class="user-input" >
 <table align="center">
  <tr>
   <td>
    <label><w:txt>Language</w:txt></label> 
   </td>
   <td>
   <w:populate using="itemForEdit">
    <select name="Locale">
     <option> </option>
     <c:forEach var="item" items="${locales}">
      <option>${item.text.rawString}</option>
     </c:forEach>
    </select>
   </w:populate>
   </td>
  </tr>
  <tr>
   <td align="center" colspan=2>
    <input type="submit" value="Update">
   </td>
  </tr>
 </table>
</form>
