<%-- Template for pages in Access Control Module. --%>
<%@ page contentType="text/html" %> <%-- must appear first! --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <tags:head/>
 <!-- override a few styles, to establish a distinct look -->
 <style type="text/css"> 
 body {
   background-color: rgb(100,200,300);
  }
 .menu-bar {
   background-color: rgb(100,200,300);
 }
 .problem { 
   font-weight: bolder; 
   background-color: red;
 }
 </style>
</head>

<body onload='showFocus()'>
<w:tooltip>
<div align="center" class='main-title'>
 <br><w:txt>Access Control</w:txt><br>
</div>
<P>

<div class="menu-bar">
 <w:txtFlow>
 <w:highlightCurrentPage styleClass='highlight'> 
  <c:url value="/access/user/UserAction.list" var="usersURL"/><A href='${usersURL}'>Users</a>
  <c:url value="/access/role/RoleAction.list" var="roleURL"/><A href='${roleURL}' >Roles</a>
  <c:url value="/access/password/ResetPasswordAction.show" var="pwdResetURL"/><A href='${pwdResetURL}' >Password Reset</a>
  <tags:commonMenuItems/>
 </w:highlightCurrentPage>
 </w:txtFlow>
</div>

<%-- Display error and information messages. --%>
<tags:displayMessages/>

<div class="body">
 <c:if test="${not empty param.TBody}">
  <jsp:include page='<%= request.getParameter("TBody") %>' flush="true"/>
 </c:if>
 <c:if test="${empty param.TBody}">
  <jsp:include page="Error.html" flush="true"/>
 </c:if>
</div>

<tags:footer/>

</body>
</w:tooltip>
</html>
