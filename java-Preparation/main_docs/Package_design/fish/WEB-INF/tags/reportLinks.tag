<%@ include file="/WEB-INF/TagHeader.jspf" %>
<%@ attribute name="reportIdx" required="true" rtexprvalue="true" %>
<c:forEach var='loopIdx' begin='1' end='4'>
 <c:choose>
   <c:when test='${loopIdx == reportIdx}'>
    <th title="Report ${loopIdx}">R${loopIdx}</th>
   </c:when>
   <c:when test='${loopIdx != reportIdx}'>
    <c:url var='reportLink' value='Report${loopIdx}Action.show'/>
    <th class='not-current' title='Report ${loopIdx}'><a href='${reportLink}'>R${loopIdx}</a></th>
   </c:when>
 </c:choose>
</c:forEach>
