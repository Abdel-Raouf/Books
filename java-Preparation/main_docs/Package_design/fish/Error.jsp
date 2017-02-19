<%-- Simple generic error page for the application. Configured in web.xml. --%>
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
<div align="center">
 <c:url var="logoURL" value="/images/logo.gif" />
 <img class="no-margin" src="${logoURL}"><br>
 <P>
 <c:url var="imageURL" value="/images/oops4.JPG" />
 <img class="no-margin" src="${imageURL}" title="Lilly's first birthday mess.">
</div>

<div class="body">
<p align="center">
 <w:txtFlow>
  <b>Sorry, we goofed.</b>
  <br>
  An unexpected error has occurred. An email describing the details 
  has likely been sent to the webmaster.
 </w:txtFlow> 
</p> 
</div>
<P>
</body>
</html>
