<%-- List-and-edit form for Supported Locales. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Allows editing of the Locales supported by this application.
  
  *Default Locale* |   
   _Not all Locales are treated equally_ : there must always be a base or default Locale, 
   which the programmer uses during development. In this example application, English 
   is that default Locale, and French is a non-default Locale. Any number of non-default 
   Locales may be added here. (This would not be possible if ResourceBundle
   was used - see [link:http://www.javapractices.com/Topic208.cjp discussion].)
   A Locale can be deleted only if there is no data "attached" to it.
  
   *Short Form*  |   
   The Short Form is used in this implementation corresponds to 
   [link:http://java.sun.com/javase/6/docs/api/java/util/Locale.html#toString() Locale.toString].
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:url value="SupportedLocaleAction.do" var="baseURL"/> 
  <form action='${baseURL}' method="post" class="user-input" name='giveMeFocus'>
   <w:populate using="itemForEdit">
   <w:txtFlow>
   <input name="Id" type="hidden">
   <table align="center">
    <tr>
     <td><label>Short Form</label> *</td>
     <td><input name="Short Form" type="text"></td>
    </tr>
    <tr>
     <td><label>Description</label> *</td>
     <td><input name="Description" type="text"></td>
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
<table class="report" title="Locales" align="center"> 
 <w:txtFlow>
  <caption>Locales</caption>
  <tr>
   <th title="Line Number">#</th>
   <th>Short Form</th>
   <th>Description</th>
  </tr>
 </w:txtFlow>
 
 <w:alternatingRow> 
  <c:forEach var="item" items="${itemsForListing}" varStatus="index">
   <tr class="row_highlight">
    <td title="Line Number">${index.count}</td>
    <td>${item.shortForm}</td>
    <td>${item.description}</td>
    <tags:editLinks baseURI='${baseURL}' id='${item.id}'/>
   </tr>
  </c:forEach>
 </w:alternatingRow>
 
</table>
