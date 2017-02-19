<%-- List-and-edit form for Translations. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  See [link:BaseTextEdit.do?Operation=List Base Text] for important information. 
 
  Coder Keys need a translation for all Locales, including the default Locale.
 
  Natural Language Keys need a translation for all Locales _except_
  for the default Locale.
  </w:txt>
 <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
 </w:txt>
 </div>
</w:help>

<c:url value="TranslationEdit.do" var="baseURL"/> 
<form action='${baseURL}' method="post" class="user-input">
  <%-- BaseText Id is always the same, and is outside the prepopulate mechanism. --%>
  <input name="BaseTextId" type="hidden" value="${parentItem.id}">
  <w:populate using="itemForEdit">
  <table align="center">
   <tr>
    <td><label>Base Text</label></td>
    <td>${parentItem.baseText}</td>
   </tr>
   <tr>
    <td><label>Is Coder Key</label></td>
    <td>${parentItem.isCoderKey}</td>
   </tr>
   <tr>
    <td><label>Locale</label> *</td>
    <td>
     <%-- editing Locale allowed only for Add. Change of Locale needs delete+add pair. --%>
   <c:choose>
     <c:when test="${not empty itemForEdit || Operation == 'Change'}">
      <%-- HTML defect: only INPUT and TEXTAREA are readonly, so SELECT cannot be used here. --%>
      <input type="hidden" name="LocaleId">
      ${itemForEdit.locale}
     </c:when>
     <c:otherwise>
      <select name="LocaleId">
       <option></option>
        <c:forEach var="item" items="${locales}">
           <option value="${item.id}">${item}</option>
       </c:forEach>
     </select>
    </c:otherwise>
   </c:choose>
    </td>
   </tr>
   <tr>
    <td><label>Translation</label> *</td>
    <td>
     <textarea name="Translation" rows="3" cols="50">
     </textarea>
    </td>
   </tr>
   <tr>
    <td align="center" colspan=2>
     <input type="submit" value="add.edit.button">
    </td>
   </tr>
  </table>
  </w:populate>
  <tags:hiddenOperationParam/>
</form>

<P>
<%-- Listing. --%>
<table class="report" title="Translations" align="center"> 
 <w:txtFlow>
 <caption>Translations</caption>
 <tr>
  <th title="Line Number">#</th>
  <th>Locale</th>
  <th>Translation</th>
 </tr>
 </w:txtFlow>
 
<w:alternatingRow> 
<c:forEach var="item" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td title="Line Number">${index.count}</td>
  <td>${item.locale}</td>
  <td> ${item.translation}</td>
  <%-- Here, the edit and delete actions cannot be impemented with editLinks.tag, since more than one id --%>
  <td align="center">
   <c:url value='TranslationEdit.do' var='editURL'>
    <c:param name='Operation' value='FetchForChange' />
    <c:param name='BaseTextId' value='${parentItem.id}' />
    <c:param name='LocaleId' value='${item.localeId}' />
   </c:url>
   <a href="${w:ampersand(editURL)}"><w:txt>edit</w:txt></a>
  </td>
  <td align="center">
   <c:url value='TranslationEdit.do' var='deleteURL'>
    <c:param name='Operation' value='Delete' />
    <c:param name='BaseTextId' value='${parentItem.id}' />
    <c:param name='LocaleId' value='${item.localeId}' />
   </c:url>
   <form action='${w:ampersand(deleteURL)}' method='POST'><input type="submit" value='Delete'></form> 
  </td>
 </tr>
</c:forEach>
</w:alternatingRow>

</table>
