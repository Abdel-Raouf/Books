<%@ page contentType="text/html" %> <%-- must appear first! --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <tags:head/>
 <!-- override a few styles, to establish a distinct look -->
 <style type="text/css"> 
 body {
   background-color: rgb(97,175,175);
 }
.menu-bar {
 background-color: rgb(97,175,175);
}
 </style>
</head>

<body onload='showFocus()'>
<w:tooltip>
<div align="center" class="main-title">
 <br><w:txt>Translation</w:txt>
</div>

<P>
<div class="menu-bar">
 <w:txtFlow>
 <w:highlightCurrentPage styleClass='highlight'> 
  <c:url value="/translate/basetext/BaseTextEdit.do?Operation=List" var="baseTextURL"/> <A HREF='${baseTextURL}'>Base Text</A>
  <c:url value="/translate/locale/SupportedLocaleAction.do?Operation=List" var="localeEditURL"/> <A HREF='${localeEditURL}'>Locales</A>
  <c:url value="/translate/translation/TranslationsList.do" var="listURL"/> <A href='${listURL}'>List</a>
  <c:url value="/translate/refresh/RefreshTranslations.do?Operation=Show" var="refreshURL"/> <A href='${refreshURL}'>Refresh</a>
  <c:url value="/translate/unknown/UnknownBaseTextEdit.do?Operation=List" var="unknownBaseTextURL"/> <A HREF='${unknownBaseTextURL}'>Unknowns</A>
  <tags:commonMenuItems/>
 </w:highlightCurrentPage>
 </w:txtFlow>
</div>

<%-- Display error and information messages. --%>
<tags:displayMessages/>

<div class="body">
 <jsp:include page='<%= request.getParameter("TBody") %>' flush="true"/>
</div>

<tags:footer/>

</body>
</w:tooltip>
</html>
