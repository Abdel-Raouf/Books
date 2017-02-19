<%-- Login Page. Configured in web.xml. --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=${web4j_key_for_character_encoding}">
 <title>
  <w:txt>${web4j_key_for_app_info.name}</w:txt></title>
  <c:url value="/stylesheet.css" var="cssURL"/>
  <link rel="stylesheet" type="text/css" href='${cssURL}' media="all">
  <tags:showFocus/>
</head>

<body onload='showFocus()'>

<div align="center">
 <c:url var="imageURL" value="/images/logo.gif" />
 <img class="no-margin" src="${imageURL}">
</div>

<div class="body">
<p align="center" style='margin-top: 5.0em;''>
 <form method="POST" action='<%= response.encodeURL("j_security_check") %>' class="user-input" name='giveMeFocus'>
 <table align="center">
 <c:if test='${not empty param["Retry"]}'>
  <tr>
   <td colspan='2' align='center'><b>Please try again/Veuillez essayer encore.</b></td>
  </tr>
  <tr>
   <td>&nbsp;</td>
  </tr>
 </c:if>
 <tr>
  <td><label>Name/Nom</label> *</td>
  <td><input type="text" name="j_username"></td>
 </tr>
 <tr>
  <td><label>Password/Mot de Passe</label></td>
  <%-- 'autocomplete' is a non-HTML attribute, supported by some browsers. Prevents prepopulation of passwords.--%>
  <td><input type="password" name="j_password" autocomplete="false"></td>
 </tr>
 <tr align="center">
  <td colspan="2"><input type="submit" value="Login"></td>
 </tr>
 </table>
 </form>
</div>

<P>
</body>
</html>

