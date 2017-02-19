<%-- Refresh translations, and show total number of translations. --%>

<w:help>
 <div class="help">
 <w:txt wikiMarkup="true" locale="en">
  Lists all translations in the database.
  
  Although there appears to be repetition of data, that is 
  not actually the case : the listing is simply a ResultSet of a 
  particular form. The underlying tables actually have no 
  repeated data. See [link:../basetext/BaseTextEdit.do?Operation=List Base Text]
  for further information.
 </w:txt>
 <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
 </w:txt>
 </div>
</w:help>

<w:txtFlow>
 <table class="report" title="Translations" align="center"> 
  <caption>Translations</caption>
  <tr>
   <th title="Line Number">#</th>
   <th>Base Text</th>
   <th>Locale</th>
   <th>Translation</th>
  </tr>
</w:txtFlow>

<w:alternatingRow> 
 <c:forEach var="item" items="${itemsForListing}" varStatus="index">
  <tr class="row_highlight">
   <td title="Line Number">${index.count}</td>
   <td> ${item.baseText}</td>
   <td>${item.locale}</td>
   <td> ${item.translation}</td>
  </tr>
 </c:forEach>
</w:alternatingRow>

 </table>


