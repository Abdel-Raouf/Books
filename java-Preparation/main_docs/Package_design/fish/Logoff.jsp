<%@page session='false' %>
<%-- Session Fixation security risk : a log off page should not send a Set-Cookie header with a new session id. --%>
<%-- After logoff, the original session is destroyed, and a new one should not be created. --%>
<%-- This page is shown after a redirect. It doesn't seem possible to present this page only in the user's preferred language. --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=${web4j_key_for_character_encoding}">
 <title>${web4j_key_for_app_info.name}</title>
  <c:url value="/stylesheet.css" var="cssURL"/>
  <link rel="stylesheet" type="text/css" href='${cssURL}' media="all">
</head>

<body>
<div align="center">
 <c:url var="imageURL" value="/images/logo.gif" />
 <img class="no-margin" src="images/logo.gif">
</div>

<div class="body">
<p align="center" style='margin-top: 5.0em;''>
  <b>You have successfully logged off.</b>
  <b>La session est fermée.</b><br>
 </p> 
<p align="center" style='margin-top: 4.0em;''>
  <c:url value="/main/home/HomePageAction.do" var="homeURL"/> <A href='${homeURL}'>Home Page/Page d'Acceuil</a>
 </p> 
 
</div>
<P>
</body>
</html>
