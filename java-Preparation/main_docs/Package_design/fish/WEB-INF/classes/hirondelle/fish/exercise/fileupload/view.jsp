<%-- Replace image file used on home page. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
   Replace the image file used on the home page.
   The file is simply overwritten. No backup copy is made. 
   
   This page exists to exercise file upload. It uses the Apache Commons FileUpload tool.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
   [A Faire]
  </w:txt>
 </div>
</w:help>

<P>
 <c:url value='ImageUploadAction.apply' var='baseURL'/>
 
 <form action='${baseURL}' enctype="multipart/form-data" class="user-input" method="POST" name='giveMeFocus'>
 <w:populate> <%-- the population mechanism ignores file upload controls --%>
 <table align="center">
 <tr>
  <td><label>Description</label> *</td>
  <td><input name="Description" type="text"></td>
 </tr>
 <tr>
  <td><label>Image File</label> *</td>
  <td><input name="ImageFile" type="file" value='C:\\temp\\lib.jpg'></td>
 </tr>
 <tr>
  <td align="center" colspan=2>
   <input type="submit" value="Update Home Page Image">
  </td>
 </tr>
 </table>
  <%-- Interesting: cannot attach this param to baseURL. Must appear here as a hidden param --%>
  <input name="Operation" type='hidden' value='Apply'>
 </w:populate>
 </form>
 
 
<P>

<table class='report'  align="center"> 
 <tr><th>Descr<th>Name<th>Mime<th>Length<th>Delete<th>Download
 <c:forEach var="image" items="${itemsForListing}">
  <tr>
   <td>${image.description}
   <td>${image.fileName}
   <td>${image.fileContentType}
   <td>${image.fileSize}
   <td><form action='ImageDeleteAction.do?Id=${image.id}' method='POST'><input type='submit' value='Delete'></form>
   <td><a href='ImageDownloadAction.do?Id=${image.id}'>view</a> 
 </c:forEach>
</table>
 
