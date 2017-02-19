<%-- Update an existing Rsvp Response. --%>

<c:url value="RsvpUpdate.apply" var="baseURL"/> 

<w:populate using="itemForEdit">
 <form action='${baseURL}' method="post" class="user-input" name='giveMeFocus'>
  <input name="VisitId" type="hidden">
  <input name="MemberId" type="hidden">
  <table align="center">
   <tr>
    <td><label>${itemForEdit.memberName} <w:txt>is coming to this lunch?</w:txt></label></td>
   </tr>
   <tr>
    <td>
     <w:txtFlow>
      <input name="Response" type="radio" value="true"> Yes
      <input name="Response" type="radio" value="false"> No
     </w:txtFlow>
    </td>
   </tr>
   <tr>
    <td align="center" colspan=2>
     <input type="submit" value="Rsvp">
    </td>
   </tr>
  </table>
 </form>
</w:populate> 

