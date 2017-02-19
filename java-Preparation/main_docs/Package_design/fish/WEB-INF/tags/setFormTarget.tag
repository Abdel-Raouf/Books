<%-- Set the target for a form. --%>
<%@ attribute name="using" required="true" rtexprvalue="true" %>
<%@ include file="/WEB-INF/TagHeader.jspf" %>
<c:choose>
 <c:when test="${not empty itemForEdit || Operation == 'Change'}">
  <c:url value="${using}.change" var="formTarget" scope='request'/> 
 </c:when>
 <c:otherwise>
  <c:url value="${using}.add" var="formTarget" scope='request'/> 
 </c:otherwise>
</c:choose>
