<%-- Show system and environment setup. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" translate="false"> 
   Various details regarding the application and its environment.
   
   Many organizations would regard this information as a "server secret". Many would even remove this 
   page altogether, in a production environment.
  </w:txt>
 </div>
</w:help>

<h3>Diagnostics</h3>
<b>WARNING : this information must be treated as a "server secret", and must be kept secure.</b> 
<b>Recommended</b> : consider removing this entire webmaster module (all classes and JSPs) 
from production environments.
<hr>

<PRE>
<b>Uptime</b> :       <fmt:formatNumber value="${uptime}"  pattern="#,##0.0000"/> days
<b>Up Since</b> :     <w:showDateTime name="web4j_key_for_start_time" pattern="YYYY-MMMM-DD hh:mm:ss" />
<b>Current Time</b> : <w:showDate pattern="yyyy-MMMM-dd HH:mm:ss z" />
<b>Application Name/Version</b> : ${web4j_key_for_app_info.name}/${web4j_key_for_app_info.version}
<b>Controller Name/Version</b> : ${controller_name_version}
<b>Build Date</b> : ${web4j_key_for_app_info.buildDate}
<b>Author</b> : ${web4j_key_for_app_info.author}
<b>Further Info</b> : ${web4j_key_for_app_info.link}
</PRE>

<table border=0 cellspacing="0" cellpadding="6" width="80%">
 <tr><td><a href="#LatestTroubleTicket">Latest Trouble Ticket</a></td></tr>
 <tr><td><a href="#ServerResponseTime">Response Time</a></td></tr>
 <tr> <td><a href="#JRE">Java Runtime Environment</a></td></tr>
 <tr> <td><a href="#JVMMemory">JVM Memory</a></td></tr>
 <tr> <td><a href="#JSPVersion">Servlet/JSP Version</a></td></tr>
 <tr> <td><a href="#Database">Database(s)</a></td></tr>
 <tr> <td><a href="#JarVersions">Jar Versions</a></td></tr>
 <tr> <td><a href="#ServletInfo">Container/Servlet Info</a></td></tr>
 <tr> <td><a href="#ContextInitParams">Context Init Params</a></td></tr>
 <tr> <td><a href="#Request">Request</a></td></tr>
 <tr> <td><a href="#Headers">Request Headers</a></td></tr>
 <tr> <td><a href="#Cookies">Cookies</a></td></tr>
 <tr> <td><a href="#ResponseEncoding">Response Encoding</a></td></tr>
 <tr> <td><a href="#SessionScopeObjects">Session Scope Objects</a></td></tr>
 <tr> <td><a href="#AppScopeObjects">Application Scope Objects</a></td></tr>
 <tr> <td><a href="#SystemProperties">System Properties</a></td></tr>
</table>


<PRE>
<a name="LatestTroubleTicket"></a><span class='diagnostic-header'>Latest Known Trouble Ticket</span> 
<c:if test="${empty web4j_key_for_most_recent_trouble_ticket}">** NONE **</c:if>
<c:if test="${not empty web4j_key_for_most_recent_trouble_ticket}"><span class="problem">** TROUBLE **</span> - see <a href="#AppScopeObjects">app scope items</a> below, where 
Trouble Ticket details are listed.

<c:url value='ClearTroubleTicket.do' var='baseURL'/>
Remove Trouble Ticket from application scope?<form method="POST" action='${baseURL}'><input type="submit" value="Yes"></form></c:if> 
<a name="ServerResponseTime"><span class='diagnostic-header'>Server Response Time</span>
${stopwatch} for the "Action" portion of this request.

<a name="JRE"></a><span class='diagnostic-header'>Java Runtime Environment</span> 
<b>JRE Version</b> : <%= System.getProperty("java.version")%> <%= System.getProperty("java.vm.info")%>
<b>JRE Default Locale</b> : <%= java.util.Locale.getDefault().toString()%>
<b>JRE Default Time Zone</b> : <%= java.util.TimeZone.getDefault().getID()%>

<a name="JVMMemory"></a><span class='diagnostic-header'>JVM Memory</span> 
<b>Total Available</b> : <fmt:formatNumber value='<%= Runtime.getRuntime().totalMemory() %>'/> bytes
<b>Used</b> : <fmt:formatNumber value='<%= Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()%>' /> bytes
<b>Unused</b> : <fmt:formatNumber value='<%= Runtime.getRuntime().freeMemory()%>' /> bytes 

<a name="JSPVersion"></a><span class='diagnostic-header'>Servlet/JSP version</span> 
<b>Servlet Version</b> : <%= application.getMajorVersion() + "." + application.getMinorVersion() %>
<b>JSP Version</b> : <%= JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion()%>

<a name="Database"></a><span class='diagnostic-header'>Database(s)</span>
<c:forEach var="item" items="${dbInfo}">
Database Name :${w:safe(item.key)} <br><c:forEach var="nvp" items="${item.value}" >${w:safe(nvp.key)} : ${w:safe(nvp.value)}<br></c:forEach>
</c:forEach> 

<a name="JarVersions"></a><span class='diagnostic-header'>Jar Versions (${fn:length(jarVersions)})</span>
<c:forEach var="entry" items="${jarVersions}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="ServletInfo"></a><span class='diagnostic-header'>Container/Servlet Info (${fn:length(containerServletInfo)})</span>
<c:forEach var="entry" items="${containerServletInfo}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="ContextInitParams"></a><span class='diagnostic-header'>Context Init Params (${fn:length(contextInitParams)})</span>
<c:forEach var="entry" items="${contextInitParams}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="Request"></a><span class='diagnostic-header'>Request (${fn:length(requestInfo)})</span> 
<c:forEach var="entry" items="${requestInfo}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="Headers"></a><span class='diagnostic-header'>Headers (${fn:length(headers)})</span>
<c:forEach var="entry" items="${headers}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="Cookies"></a><span class='diagnostic-header'>Cookies (${fn:length(cookies)})</span>
<c:forEach var="entry" items="${cookies}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="ResponseEncoding"></a><span class='diagnostic-header'>Response Encoding (${fn:length(responseEncoding)})</span>
<c:forEach var="entry" items="${responseEncoding}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="SessionScopeObjects"></a><span class='diagnostic-header'>Session-scope objects (${fn:length(sessionScopeItems)})</span>
<c:forEach var="entry" items="${sessionScopeItems}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<%-- Seen to throw a NullPointerException in Jetty 6.1. TagLibraryInfoImpl.toString() method throws the exception. --%>
<a name="AppScopeObjects"></a><span class='diagnostic-header'>App-scope objects  (${fn:length(appScopeItems)})</span>
<c:forEach var="entry" items="${appScopeItems}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>

<a name="SystemProperties"></a><span class='diagnostic-header'>System Properties (${fn:length(systemProperties)})</span>
<c:forEach var="entry" items="${systemProperties}"><b>${w:safe(entry.key)}</b> = ${w:safe(entry.value)}
</c:forEach>
</PRE>
