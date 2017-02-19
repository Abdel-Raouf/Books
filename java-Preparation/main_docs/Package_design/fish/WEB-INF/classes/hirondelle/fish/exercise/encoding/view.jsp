<%-- Replace image file used on home page. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
   Simple page for exploring encoding issues in the application and environment.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
   [A Faire]
  </w:txt>
 </div>  
</w:help>

<P>

<PRE>
This page is a debugging tool. 
It explores the encoding attributes of your application and its environment.
The UTF8 encoding is highly recommended.

<b>Browser Request Headers</b>
accept-charset : <%= request.getHeader("accept-charset") %>
accept-language : <%= request.getHeader("accept-language") %>

<b>All Browser Request Headers</b>
<c:forEach var="entry" items="${requestHeaders}">${w:safe(entry.key)} = ${w:safe(entry.value)}
</c:forEach>
<b>JRE Settings</b>
JRE Version : <%= System.getProperty("java.version")%> <%= System.getProperty("java.vm.info")%>
JRE Locale : <%= java.util.Locale.getDefault().toString()%>
JRE File Encoding : <%= System.getProperty("file.encoding") %>

<b>Container Settings</b>
Not accessible here. Check manually.
For Tomcat, check encoding settings in [TOMCAT_HOME]/conf/web.xml.

<b>Database Settings</b>
Not accessible here. Check manually.
Here are some useful commands for MySQL :
show variables like "character_set_database";
show variables like "collation_database";

<b>Application Defaults In web.xml</b> 
Encoding (web4j_key_for_character_encoding) : ${web4j_key_for_character_encoding}
Locale (web4j_key_for_locale) : ${web4j_key_for_locale}

Request encoding (as set by Controller): <%= request.getCharacterEncoding() %>
Response encoding (as set by Controller): <%= response.getCharacterEncoding() %>

<b>JSP Page Encoding</b>
Not available here. (JSPs should usually be saved as UTF8.)
Controls how server compiles the JSP.
Can specify in web.xml using jsp-config.
Can also specify using pageEncoding attribute of page directive.

<b>Literal Text In This JSP</b>
English: This is English.
French: être, à, désolé

Hebrew 
 should be :   &#x05D0;
 rendered as : א

Czech 
 should be :   Jako efektivn&#x115;j&#x161;&#xED; se n&#xE1;m jev&#xED; po&#x159;&#xE1;d&#xE1;n&#xED; tzv.
 rendered as : Jako efektivnější se nám jeví pořádání tzv.

Arabic
 should be :    &#x06A4;
 rendered as :  ڤ

Fun Characters
  Heart : &#x2665;
  Music : &#x266B;

Chinese: &#26085;&#26412;&#35486;

<b>Logs</b>
What is the encoding of your log files?

<b>Test encoding of form data</b>
<form method='GET' action='EncodingAction.show'>
 <input type='text' name='Test'>
 <input type='submit'>
</form>


</PRE>

<b>Reminders :</b>
<ul>
<li>all modern browsers support UTF8
<li>most mobile devices support UTF8
<li>static pages don't go through the Controller
<li>the charset portion of Content-Type header is a misnomer, and misleading. It's really an <em>encoding</em>.
<li>correct display requires a font that is able to render the text
<li>escapes can always be used (as shown above), but should be used only as a last resort
</ul>