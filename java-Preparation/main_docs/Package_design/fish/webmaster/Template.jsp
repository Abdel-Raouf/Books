<%@ page contentType="text/html" %> <%-- must appear first! --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <tags:head/>
 <!-- override a few styles, to establish a distinct look -->
 <style type="text/css"> 
 body {
   background-color: rgb(200,200,300);
  }
 .menu-bar {
   background-color: rgb(200,200,300);
 }
 .problem { 
   font-weight: bolder; 
   background-color: red;
 }
 </style>
</head>

<body onload='showFocus()'>
<w:tooltip>
<div align="center" class="main-title">
 <br> <w:txt>Webmaster</w:txt><br>
</div>
<P>
<div class="menu-bar">
 <w:txtFlow>
 <w:highlightCurrentPage styleClass='highlight'> 
  <c:url value="/webmaster/performance/ShowPerformance.do" var="performanceURL"/><A HREF='${performanceURL}'>Performance</A>
  <c:url value="/webmaster/diagnostics/ShowDiagnostics.do" var="diagnosticsURL"/><A href='${diagnosticsURL}'>Diagnostics</a>
  <c:url value="/webmaster/logging/EditLoggerLevels.do?Operation=List" var="loggerLevelsURL"/><A href='${loggerLevelsURL}' >Loggers</a>
  <c:url value="/webmaster/testfailure/ForceFailure.do" var="forceFailureURL"/><A HREF='${forceFailureURL}'>Failure Test</A>
  <c:url value="/webmaster/ping/Ping.do" var="pingURL"/><A HREF='${pingURL}' >Ping</A>
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
