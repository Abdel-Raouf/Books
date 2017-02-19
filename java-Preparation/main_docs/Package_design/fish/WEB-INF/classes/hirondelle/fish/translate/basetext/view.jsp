<%-- List-and-edit form for Base Text. --%>

<w:help>
 <div class="help">
  <b>Base Text</b><br>
  The Base Text is the text that is to be translated. The programmer works 
  in Base Text, and almost always codes as if the application uses 
  only a single language (in the default Locale - in this example, English).
  For example, all messages that an Action may emit are in Base Text. 
  Base Text comes in two forms - Natural Language keys, and Coder Keys.
  
  <P><b>Natural Language Key</b><br>
  This is the preferred style for the programmer - simply to use natural 
  language text (in the default Locale of the application). 
  For example, an Action might respond with a message to the end user, 
  using a Natural Language key : 
  <PRE>
  addMessage("Item deleted successfully.");
  </PRE>
  This allows code to be more legible 
  at the point of call, while still allowing for full translation.
  A Natural Language Key needs translation for all Locales <em>except for the 
  default Locale</em>.
  
  <P><b>Coder Key</b><br>
  A Coder Key is text such as 'add.button'. This text is meant only for the 
  programmer. It is not intended for the end user, in any language. A 
  Coder Key needs translation into all target languages, <em>including the 
  default Locale</em>.
  
  <P><b>Simple Versus Compound Messages</b><br>
  Simple messages have no parameters, as in
  <PRE>
    Item added successfully.
  </PRE>
  Compound messages, on the other hand, take data as parameters. Special place holders 
  are used for the parameters, as in
  <PRE>
  On _1_, I will go to a restaurant named _2_ for lunch.
  </PRE>
  Here, <tt>_1_</tt> stands for a date, while <tt>_2_</tt> stands for the name of a restaurant.
  
  <P>These place holders start at <tt>_1_</tt>, and increase from there. 
  The order of the parameters can differ between Locales, but each the message 
  must have the same number of parameters across all of its Locales.
 </div>
</w:help>

<c:url value="BaseTextEdit.do" var="baseURL"/> 
<form action='${baseURL}' method="post" class="user-input">
  <w:populate using="itemForEdit">
  <w:txtFlow>
   <input name="Id" type="hidden">
   <table align="center">
    <tr>
     <td>
      <label>Base Text</label> *<br>
      <textarea name="Base Text" cols="40" rows="3"></textarea>
     </td>
    </tr>
    <tr>
     <td>
      <label>Is Coder Key</label>
      <input type="checkbox" name="Is Coder Key" value="true">  
     </td>
    </tr>
    <tr>
     <td align="center" colspan=2>
      <input type="submit" value="add.edit.button">
     </td>
    </tr>
   </table>
  </w:txtFlow>
  </w:populate>
  <tags:hiddenOperationParam/>
</form>

<P>
<%-- Listing. --%>
<table class="report" title="Base Text" align="center"> 
 <w:txtFlow>
  <caption>Base Text</caption>
  <tr>
   <th title="Line Number">#</th>
   <th>Base Text</th>
   <th>Is Coder Key</th>
  </tr>
 </w:txtFlow>
 
 <w:alternatingRow> 
  <c:forEach var="item" items="${itemsForListing}" varStatus="index">
   <tr class="row_highlight">
    <td title="Line Number">${index.count}</td>
    <td> ${item.baseText}</td>
    <c:if test="${item.isCoderKey}">
     <td><w:txt>Yes</w:txt></td>
    </c:if>
    <c:if test="${not item.isCoderKey}">
     <td><w:txt>-</w:txt></td>
    </c:if>
    <tags:editLinks baseURI='${baseURL}' id='${item.id}'/>
    <td align="center">
     <c:url value='../translation/TranslationEdit.do' var='translationsURL' >
      <c:param name='Operation' value='List' />
      <c:param name='BaseTextId' value='${item.id}' />
     </c:url>
     <a href="${w:ampersand(translationsURL)}"><w:txt>translations</w:txt></a>
    </td>
   </tr>
  </c:forEach>
 </w:alternatingRow>
 
</table>
