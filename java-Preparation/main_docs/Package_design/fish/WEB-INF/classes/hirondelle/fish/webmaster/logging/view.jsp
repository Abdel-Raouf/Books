<%-- Edit JDK Logger levels. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
   Set logging levels for both Loggers and their Handlers.|
   
   Loggers inherits settings from their ancestors. They also inherit the _Handlers_ attached to their ancestors.
   For more information, see the logging 
   [link:http://java.sun.com/javase/6/docs/api/java/util/logging/package-summary.html API] and 
   [link:http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html overview].|
   
   The root logger is named with an empty string, and appears first in the listing below.
   The logger named this 
   [link:http://java.sun.com/javase/6/docs/api/java/util/logging/Logger.html#GLOBAL_LOGGER_NAME global]
   is meant for only the most casual uses, and should likely be avoided.|
   
   This application does some custom setup of logging upon startup: a FileHandler is created and attached to 
   the 'base' package. That is, a logging file specific to this application is created, and is _not_ shared 
   with any other application. See the StartupLoggingConfig class for more information.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
   [A Faire]
  </w:txt>
  
 </div>
</w:help>

<c:if test="${not empty itemForEdit || Operation =='Change' }">
<P>
 <w:populate using="itemForEdit">
  <c:url value='EditLoggerLevels.do?Operation=Change' var='baseURL'/>
 <form action='${baseURL}' class="user-input" method="POST" name='giveMeFocus'>
<table align="center">
 <tr>
  <td><label>Logger</label></td>
  <td><input name="Name" type="text" readonly="readonly" size="50"></td>
 </tr>
 <tr>
  <td><label>Level</label></td>
  <td> 
  <select name="Level">
    <option></option>
      <option>OFF</option>
      <option>INHERIT FROM PARENT</option>
      <option>SEVERE</option>
      <option>WARNING</option>
      <option>INFO</option>
      <option>CONFIG</option>
      <option>FINE</option>
      <option>FINER</option>
      <option>FINEST</option>
      <option>ALL</option>
  </select>
  </td>
 </tr>
 <tr>
  <td align="center" colspan=2>
   <input type="submit" value="Change Level">
  </td>
 </tr>
 </table>
 </form>
 </w:populate>
</c:if>

<P>
<table class="report" title="Loggers" align="center"> 
 <caption>JRE Loggers</caption>
 <tr>
  <th title="Logger Name">Name</th>
  <th title="Logger Level">Level</th>
  <th title="Handlers">Handlers</th>
 </tr>
<w:alternatingRow> 
<c:forEach var="item" items="${itemsForListing}">
 <tr class="row_highlight">
   <td>
    <b>
     <c:if test="${not empty item.key}">${item.key}</c:if>
     <c:if test="${empty item.key}">[Empty String - Root Logger]</c:if>
    </b>
   </td>
   <td title="Alter this Logger's Level">
    <c:url value='EditLoggerLevels.do' var='editLink' >
     <c:param name='Operation' value='FetchForChange' />
     <c:param name='Name' value='${item.key}' />
    </c:url>
    <a href='${w:ampersand(editLink)}'>
     <c:if test="${not empty item.value.level}">
       ${item.value.level}
     </c:if>
     <c:if test="${empty item.value.level}">
       ...
     </c:if>
    </a>
   </td>
   <td>
    <c:forEach var="handler" items="${item.value.handlers}">   
     ${handler.class.name}(${handler.level})<br>
    </c:forEach>
   </td>
  </tr>
</c:forEach>
</w:alternatingRow>
</table>
<P>

