<%@ include file="/WEB-INF/TagHeader.jspf" %>
  <c:url value="/all/preferences/PreferencesAction.show" var="preferencesURL"/><A href='${preferencesURL}' title='Change your preferences'>Preferences</a>
  <c:url value="/help/ShowHelpAction.do" var="showHelpURL"> 
   <c:param name="OriginalURI">${web4j_key_for_current_uri}</c:param> 
  </c:url>
  <%-- The help mechanism is supported only for GETs. --%>
  <c:if test="${pageContext.request.method eq 'GET'}">
    <a href='${w:ampersand(showHelpURL)}' title='Toggle Display of Help Text'>Help</a>
  </c:if>
  <c:url value="/all/logoff/LogoffAction.apply" var="logoffURL"/><form action='${logoffURL}' method='POST' class='log-off'> <input type='submit' value='Log Off' title='Log Off Fish and Chips Club'></form>
