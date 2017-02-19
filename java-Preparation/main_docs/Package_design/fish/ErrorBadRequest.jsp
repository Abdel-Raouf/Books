<%-- Error page for malformed requests. Configured in web.xml. --%>
<%@page session='false' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=${web4j_key_for_character_encoding}">
 <title>
  <w:txt>${web4j_key_for_app_info.name}</w:txt></title>
  <c:url value="/stylesheet.css" var="cssURL"/>
  <link rel="stylesheet" type="text/css" href='${cssURL}' media="all">
</head>
<body>

<div class="body">
<div align="center" class='main-title'>
 <img class="no-margin" src="../../images/<w:txt value='logo.gif'/>" alt="Fish And Chips Club">
</div>
<P>

<h3>Problem Detected</h3>
 A problem with the underlying HTTP request has been detected.
  
  <P>Possible problems include :
  <ul>
   <li>the request URL has an unexpected form
   <li>the request includes spam
   <li>the request has an unexpectedly large size
   <li>the request includes an unexpected request parameter
   <li>the request uses a 'GET', and should use a 'POST' instead
  </ul>
</div>

<P>

</body>
</html>
