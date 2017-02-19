<%@page session='false' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<%-- This style refers to web.xml mapping. --%>
<%-- <%@ taglib prefix="w" uri="/web4j" %> --%>

<%-- This style refers to the .tld directly, without using the mapping defined in web.xml. --%>
<%-- The web.xml mapping may be removed when this style is used. --%>
<%-- <%@ taglib prefix="w" uri="/WEB-INF/tlds/web4j.tld" %> --%>

<html>
<head><title>Test Page For Custom Tags</title></head>
<body>

<h3>Test Page For Custom Tags</h3>

Simple test page to confirm tag libraries behave as expected.

<P>There is a small security risk associated with this page -- it gives some details regarding implementation, but does not require login.
It is highly recommended that this page be <b>deleted</b> once the configuration of custom tags is confirmed.

<P>The logic for locating underlying .tld files can be direct or indirect :
<ul>
 <li>taglib directive - maps prefixes to uri's (indirect), or directly to underlying .tld files
 <li>taglib entry in web.xml - maps uri's to underlying .tld files 
</ul>
  
<P>If the .tld file cannot be located, then a 'fatal translation error' results. 
That is, it will not be possible to generate a valid .java file from the JSP, 
and an error page or a blank page will be sent as the response.

<hr>

<P>JSTL formatting tag &lt;fmt&gt; : <fmt:formatNumber value="9543.21" type="currency"/>

<P>JSTL conditional tag &lt;c:if&gt; : <c:if test="true">OK</c:if>

<P>JSTL link tag &lt;c:url&gt;: <c:url value="/stylesheet.css" var="testURL"/><a href='${testURL}'>Test link</a>

<P>JSTL function fn:length(null) : ${fn:length(null)}

<P>WEB4J &lt;w:showDate&gt; tag : <w:showDate/>

<P>WEB4J &lt;w:txt&gt; tag : <w:txt>OK</w:txt>


</body>
</html>
