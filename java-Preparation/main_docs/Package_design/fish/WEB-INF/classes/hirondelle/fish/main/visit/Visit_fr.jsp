<%-- List-and-edit form for weekly Visits to a Resto. --%>

<c:set value='VisitAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

 <w:populate using="itemForEdit">
  <form action='${formTarget}' method="post" class="user-input">
   <input name="Id" type="hidden">
   <table align="center">
    <tr>
     <td><label>[FRENCH] Restaurant</label>*</td>
     <td>
      <select name="Restaurant">
       <option> </option>
        <c:forEach var="resto" items="${restos}">
          <option value='${resto.id}'>${resto.text.rawString}</option>
        </c:forEach>
      </select>
     </td>
    </tr>
    <tr>
     <td><label>Date</label>*</td>
     <td><input name="Lunch Date" type="text"> (MMDDYYYY)</td>
    </tr>
    <tr>
     <td><label>Message</label></td>
     <td><input name="Message" type="text"></td>
    </tr>
    <tr>
     <td align="center" colspan=2>
      <input type="submit" value="Mettre a Jour">
     </td>
    </tr>
    </table>
 </form>
</w:populate>


<%-- Sortable listing of all Visits. --%>
<P>
<table class="report" title="Visites" align="center"> 
 <caption>Visites</caption>
 <c:set value="${baseURI}?Operation=List" var="baseSortingURI"/>
 <tr>
  <th>Date <tags:sortLinks column="LunchDate" baseURI="${baseSortingURI}" /></th>
  <th>Restaurant <tags:sortLinks column="Resto.Name" baseURI="${baseSortingURI}" /></th>
  <th>Message <tags:sortLinks column="Message" baseURI="${baseSortingURI}"/></th>
 </tr>

<w:alternatingRow> 
 <c:forEach var="visit" items="${itemsForListing}" varStatus="index">
  <tr class="row_highlight">
   <c:set value="${visit.lunchDate}" var="lunchDate"/>  
   <td><w:showDate name="lunchDate" pattern='yyyy-MM-dd'/></td>  
   <td>${visit.restaurant}</td>
   <td>${visit.message}</td>  
   <tags:editLinksFineGrained baseURL='${baseURL}' id='${visit.id}'/>
  </tr>
 </c:forEach>
</w:alternatingRow> 

</table>
