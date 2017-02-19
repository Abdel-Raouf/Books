<%-- Refresh translations, and show total number of translations. --%>

<w:help>
 <div class="help">
 <w:txt wikiMarkup="true" locale="en">
  This application's implementation of 
  [link:http://www.javapractices.com/apps/web4j/javadoc/hirondelle/web4j/ui/translate/Translator.html Translator]
  reads in translations upon startup, and holds them in memory. After using other pages to update the 
  database translation tables, you must reload this page to reflect the updated translations.
 </w:txt>
 <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
 </w:txt>
 </div>
</w:help>

<div align="center">
 <w:txt>Hit button to reload translations from database into memory.</w:txt><br>
</div>
<P>
<div align="center">
 <c:url value='RefreshTranslations.do' var='baseURL'>
  <c:param name='Operation' value='Apply'/>
 </c:url>
 <form action='${baseURL}' method='POST' align='center'><input type='submit' value='<w:txt value="Reload Translations"/>'></form>
 <P>
 <w:txt>Current number of translations</w:txt> : <b>${numTranslations}</b><br>
</div>


