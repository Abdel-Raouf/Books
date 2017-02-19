<%-- Tag file: up and down arrows for sorting a listing.--%>
<%-- If the 'baseURI' has no params, it can be passed as BLANK, since c:url knows its context. --%>
<%@ attribute name="column" required="true" rtexprvalue="true" %>
<%@ attribute name="baseURI" required="true" rtexprvalue="true" %>
<%@ include file="/WEB-INF/TagHeader.jspf" %>
<c:url value="${baseURI}" var="ascURI">
 <c:param name='SortOn' value='${column}'/>
 <c:param name='Order' value='ASC'/>
</c:url>
<c:url value="${baseURI}" var="descURI"> 
 <c:param name='SortOn' value='${column}'/>
 <c:param name='Order' value='DESC'/>
</c:url>
<a href="${w:ampersand(ascURI)}" title="Ascending"><img src="../../images/up.gif" border="0" alt='Ascending'></a> 
<a href="${w:ampersand(descURI)}" title="Descending"><img src="../../images/down.gif" border="0" alt='Descending'></a> 
