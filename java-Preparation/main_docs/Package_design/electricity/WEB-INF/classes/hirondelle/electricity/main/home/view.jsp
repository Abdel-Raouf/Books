<c:set value='SpendingAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

<%-- Form for adds and edits. --%>
<form action='${formTarget}' method="post" class="user-input"> 
  <w:populate using="itemForEdit"> 
   <input name="Id" type="hidden">
   <table align="center">
    <tr title='Format: YYYYMMDD' >
     <td><label>Date Paid *</label></td>
     <td><input name="DatePaid" type="text" maxlength='12'> YYYYMMDD</td>
    </tr>
    <tr title='Range 0.00..1000.00. Includes taxes. Two decimals.'>
     <td><label>Payment *</label></td>
     <td><input name="Payment" type="text"  maxlength='10'></td>
    </tr>
    <tr title='Range 0.1..500.0. One decimal.'>
     <td><label>KWH *</label></td>
     <td><input name="KilowattHours" type="text"  maxlength='7'></td>
    </tr>
    <tr title='Sometimes the meter reading is estimated'>
     <td><label>Estimated Reading?</label></td>
     <td><input name="IsEstimated" type="checkbox" value='true'>Yes</td>
    </tr>
    <tr title='Type of building'>
     <td><label>BuildingType *</label></td>
     <td>
       <select name='Facility'>
         <option></option>
         <c:forEach items='${facilities}' var='item'>
          <option value='${item.id}'>${item}</option>
         </c:forEach>
       </select>
     </td>
    </tr>
    <tr title='Up to 100 characters'>
     <td><label>Comment</label></td>
     <td><input name="Comment" type="text" maxlength='100'></td>
    </tr>
    <tr>
     <td align="center" colspan=2>
      <input type='submit' value="Add/Edit">
     </td>
    </tr>
   </table>
  </w:populate>
</form>

<P>
<%-- Listing of all items. --%>
<table class="report" title="Spending" align="center"> 
 <caption>Electricity Spending</caption>
 <tr>
  <th title="Line Number">#</th>
  <th title='Date Paid'>Date Paid</th>
  <th title='Amount Paid'>Payment</th>
  <th title='Kilowatt Hours'>KWH</th>
  <th title='Estimated Reading'>Est</th>
  <th title='Amount Per KWH'>Per KWH</th>
  <th>Building Type</th>
  <th>Comment</th>
 </tr>
<w:alternatingRow> 
<c:forEach var="item" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td title="Line Number">${index.count}</td>
  <td align='right'>
    <c:set value="${item.datePaid}" var="paidOn"/>
    <w:showDateTime name='paidOn' pattern='MMM DD YYYY'/>    
  </td>
  <td align="right">
   <fmt:formatNumber value="${item.payment}" pattern='#,##0.00'/>
  </td>
  <td align='right'>
   <fmt:formatNumber value="${item.kilowattHours}" pattern='#,##0.0'/> 
  </td>
  <td align='left'>
   <c:if test="${item.isEstimated}"><span title='Estimated Reading'>E</span></c:if>
  </td>
  <td align='right'>
    <fmt:formatNumber value="${item.paymentPerKWH}" pattern='#,##0.00'/> 
  </td>
  <td align='left'>
   ${item.facility} 
  </td>
  <td align="left">${item.comment}</td>
  <tags:editLinks baseURL='${baseURL}' id='${item.id}'/>
 </tr>
</c:forEach>
</w:alternatingRow>
</table>

