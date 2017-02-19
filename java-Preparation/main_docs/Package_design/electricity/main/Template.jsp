<%@ page contentType="text/html" %> <%-- must appear first! --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <tags:head/>
</head>
 
<body>
<P>
 <div class='menu-bar'>
  <c:url value="/index.html" var="homeURL"/> <A href='${homeURL}' title='Home page'>Home</a>
  <c:url value="/main/home/SpendingAction.list" var="listURL"/> <A href='${listURL}' title='List and edit your electricity spending'>Edit</a>
 </div>
 
<%-- Display error and information messages. --%>
<tags:displayMessages/>

<div class="body">
 <c:if test="${not empty param.TBody}">
 <jsp:include page='${param.TBody}' flush="true"/>
 </c:if>
 <c:if test="${empty param.TBody}">
  <jsp:include page="Error.html" flush="true"/>
 </c:if>
</div>

<tags:footer/>

</body>
</html>
