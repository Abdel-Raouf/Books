<%-- Generate links to edit and delete items in a listing. --%>
<%@ attribute name="baseURL" required="true" rtexprvalue="true" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ include file="/WEB-INF/TagHeader.jspf" %>
  <td align="center">
    <c:url value='${baseURL}.fetchForChange' var='editURL' >
    <c:param name='Id' value='${id}' />
   </c:url>
   <a href='${w:ampersand(editURL)}'><w:txt>edit</w:txt></a>
  </td>
  <td align="center">
   <c:url value='${baseURL}.delete' var='deleteURL' >
    <c:param name='Id' value='${id}' />
   </c:url>
   <form action='${w:ampersand(deleteURL)}' method='POST'><input type="submit" value='Delete'></form> 
  </td>
