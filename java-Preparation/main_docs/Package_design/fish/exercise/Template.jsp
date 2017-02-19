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
 <br><w:txt>Exercise Features</w:txt><br>
</div>

<P>
<div class="menu-bar">
 <w:txtFlow> 
  <w:highlightCurrentPage styleClass='highlight'> 
  <c:url value="/exercise/multivalued/ToppingsAction.show" var="toppingsURL"/><A HREF='${toppingsURL}' >Toppings</A>
  <c:url value="/exercise/fileupload/ImageUploadAction.show" var="uploadURL"/><A HREF='${uploadURL}' >Upload</A>
  <c:url value="/exercise/encoding/EncodingAction.show" var="encodingURL"/><A HREF='${encodingURL}' >Encoding</A>
  <c:url value="/exercise/binary/ServeBinaryAction.rtf" var="binaryURL"/><A HREF='${binaryURL}' >Serve Binary</A>
  <c:url value="/exercise/html5/PersonAction.list" var="html5URL"/><A HREF='${html5URL}' >HTML5 Forms</A>
  <tags:commonMenuItems/>
 </w:highlightCurrentPage> 
 </w:txtFlow>
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
</w:tooltip>
</html>