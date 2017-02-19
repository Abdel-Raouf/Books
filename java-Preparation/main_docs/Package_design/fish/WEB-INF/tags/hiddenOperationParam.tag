<%@ include file="/WEB-INF/TagHeader.jspf" %>
<%-- Control the "mode" of the form, Add versus Edit. Uses conventional name 'itemForEdit'. --%>
<%-- The Operation=change case occurs when a POSTed edit fails, and original input needs recycling. --%>
<c:choose>
 <c:when test="${not empty itemForEdit || Operation == 'Change'}">
  <input type="hidden" name="Operation" value="Change">
 </c:when>
 <c:otherwise>
  <input type="hidden" name="Operation" value="Add">
 </c:otherwise>
</c:choose>
