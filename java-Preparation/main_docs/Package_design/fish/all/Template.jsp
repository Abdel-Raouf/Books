<%-- Template for pages in 'ALL' Module. --%>
<%@ page contentType="text/html" %> <%-- must appear first! --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <tags:head/>
 <!-- override a few styles, to establish a distinct look -->
 <style type="text/css"> 
 .problem { 
   font-weight: bolder; 
   background-color: red;
 }
 </style>
</head>

<body onload='showFocus()'>
<w:tooltip>
<div align="center" class='main-title'>
 <br><w:txt>Preferences</w:txt><br>
</div>
<P>

<div class="menu-bar">
 <w:txtFlow>
 <w:highlightCurrentPage styleClass='highlight'>  
  <c:url value="/all/preferences/PreferencesAction.show" var="preferencesURL"/> <A href='${preferencesURL}'>Preferences</a>
  <c:url value="/all/password/ChangePasswordAction.show" var="changePassURL"/> <A href='${changePassURL}'>Change Password</a>
  <w:show ifRole="user-president,user-general"><c:url value="/main/home/HomePageAction.show" var="homeURL"/> <A href='${homeURL}'>Home</a></w:show>
  <w:show ifRole="translator"><c:url value="/translate/locale/SupportedLocaleAction.do?Operation=List" var="translatorURL"/> <A href='${translatorURL}'>Translations</a></w:show>
  <w:show ifRole="webmaster"><c:url value="/webmaster/performance/ShowPerformance.do" var="webmasterURL"/> <A href='${webmasterURL}'>Webmaster</a></w:show>
  <w:show ifRole="access-control"><c:url value="/access/user/UserAction.list" var="accessControlURL"/> <A href='${accessControlURL}'>Users</a></w:show>
  <c:url value="/help/ShowHelpAction.do" var="showHelpURL"> 
   <c:param name="OriginalURI">${web4j_key_for_current_uri}</c:param> 
  </c:url>
  <a href='${w:ampersand(showHelpURL)}'>Help</a>
  <c:url value="/all/logoff/LogoffAction.do" var="logoffURL"/><form action='${logoffURL}' method='POST' class='log-off''><input type='submit' value='Log Off'></form>
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
