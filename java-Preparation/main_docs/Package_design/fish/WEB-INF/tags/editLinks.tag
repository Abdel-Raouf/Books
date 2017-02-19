<%-- Generate links to edit and delete items in a listing. --%>
<%@ attribute name="baseURI" required="true" rtexprvalue="true" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ include file="/WEB-INF/TagHeader.jspf" %>
  <td align="center">
    <c:url value='${baseURI}' var='editURL' >
    <c:param name='Operation' value='FetchForChange' />
    <c:param name='Id' value='${id}' />
   </c:url>
   <a href='${w:ampersand(editURL)}'><w:txt>edit</w:txt></a>
  </td>
  <td align="center">
   <c:url value='${baseURI}' var='deleteURL' >
    <c:param name='Operation' value='Delete' />
    <c:param name='Id' value='${id}' />
   </c:url>
   <form action='${w:ampersand(deleteURL)}' method='POST'><input type="submit" value='Delete'></form> 
  </td>
