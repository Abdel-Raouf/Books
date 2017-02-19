<%-- Add a new Rsvp Response. --%>

<c:url value="RsvpAdd.do?Operation=Apply" var="baseURL"/> 
<%-- Here, a "stub" rsvp object is used for the Add operation. This stub --%>
<%-- more or less carries id information only, with a blank yes/no rsvp response. --%>
<w:populate using="itemForEdit">
<form action='${baseURL}' method="post" class="user-input" name='giveMeFocus'>
 <input name="VisitId" type="hidden">
 <input name="MemberId" type="hidden">
 <table align="center">
  <tr>
   <td><label>${itemForEdit.memberName} <w:txt>is coming to this lunch?</w:txt></label></td>
  </tr>
  <tr>
   <w:txtFlow>
    <td>
     <input name="Response" type="radio" value="true"> Yes
     <input name="Response" type="radio" value="false"> No
    </td>
   </w:txtFlow>
  </tr>
  <tr>
   <td align="center" colspan=2>
    <input type="submit" value="Rsvp">
   </td>
  </tr>
 </table>
</form>
</w:populate>

