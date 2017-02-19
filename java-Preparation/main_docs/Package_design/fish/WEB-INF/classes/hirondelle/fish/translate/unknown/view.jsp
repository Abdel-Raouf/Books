<%-- List Unknown BaseText items, and decide what to do with them. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Lists all Base Text that is unknown to the 
  [link:http://www.javapractices.com/apps/web4j/javadoc/hirondelle/web4j/ui/translate/Translator.html Translator]
  (more precisely, the in-memory version of the Translator). 

  This screen is especially useful during development, since it allows the Base Text to be 
  gathered *simply by exercising the application*. The general steps are :|
 ~ develop a feature as if the application has only a single language.
 ~ during final unit testing of the feature, re-start the application and exercise 
   it to ensure all possible textual variations are emitted, including all error 
   messages. 
 ~ at this stage, all text intended for translation will be found and 
   "recorded" in memory as "unknown base text".
 ~ hit the "Stop" button on this page to stop the "recording" of unknown base text. This will 
   persist the unknown items into the database.
 ~ view the listing on this screen and decide what to do with each item : ignore it, treat is as a 
   coder key, or as a natural language key. (The actual translation of such keys into other languages is 
   performed later.)
 ~ _when the listing is emptied_, you have the option of repeating the process, by 
   hitting the "Start" button on this page.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
   [A Faire.]
  </w:txt>
 </div>
</w:help>

<%-- Buttons to start/stop of Recording of unknowns. --%>
<c:url value="UnknownBaseTextRecorder.do" var="recordingURL"/>  
<c:if test="${isRecording}">
  <form action='${recordingURL}' method="post" class="user-input">
   <table align="center">
    <tr>
      <td>
        <input type="submit" value="stop.button">
        <input name="Operation" type="hidden" value="Stop">
      </td>
    </tr>
   </table>
  </form>  
</c:if>

<c:if test="${not isRecording && empty itemsForListing}">
  <form action='${recordingURL}' method="post" class="user-input">
   <table align="center">
    <tr>
      <td>
       <input name="Operation" type="hidden" value="Start">
       <input type="submit" value="start.button">
      </td>
    </tr>
   </table>
  </form>  
</c:if>

<P>
<%-- Listing. --%>
<table class="report" title="Unknown Base Text" align="center"> 
 <w:txtFlow>
 <caption>Unknown Base Text</caption>
 <colgroup> 
  <col width="5%">
  <col width="65%">
  <col width="10%">
  <col width="10%">
  <col width="10%">
 </colgroup>
 <tr>
  <th title="Line Number">#</th>
  <th>Unknown Base Text</th>
 </tr>
 </w:txtFlow>
 
<c:url value="UnknownBaseTextEdit.do" var="baseURL"/>  
<w:alternatingRow> 
<c:forEach var="item" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td title="Line Number">${index.count}</td>
  <td> ${item.baseText}</td>
  <td align="center">
   <form action='${baseURL}' method="post" class="user-input">
    <input name="Operation" type="hidden" value="Add">
    <input name="IsCoderKey" type="hidden" value="false">
    <input name="BaseText" type="hidden" value="${item.baseText}">
    <input type=submit value='add.button'>
   </form>  
  </td>
  <td align="center">
   <form action='${baseURL}' method="post" class="user-input">
    <input name="Operation" type="hidden" value="Add">
    <input name="IsCoderKey" type="hidden" value="true">
    <input name="BaseText" type="hidden" value="${item.baseText}">
    <input type=submit value='add.as.coder.key.button'>
   </form>  
  </td>
  <td align="center">
   <form action='${baseURL}' method="post" class="user-input">
    <input name="Operation" type="hidden" value="Delete">
    <input name="IsCoderKey" type="hidden" value="false">
    <input name="BaseText" type="hidden" value="${item.baseText}">
    <input type=submit value='ignore.button'> 
   </form>  
  </td>
 </tr>
</c:forEach>
</w:alternatingRow>
</table>

