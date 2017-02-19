<%@ include file="/WEB-INF/TagHeader.jspf" %>

<%-- Display error and information messages. --%>
<p class="display-messages">
 <c:if test="${not empty web4j_key_for_errors}"> 
  <span class="error">Oops!</span>
  <w:messages name="web4j_key_for_errors">
   <span class="error">placeholder</span><br>
  </w:messages>
  <c:remove var="web4j_key_for_errors" scope ="session"/>
 </c:if>

 <c:if test="${not empty web4j_key_for_messages}"> 
  <w:messages name="web4j_key_for_messages">
   <span class="message">placeholder</span><br>
  </w:messages>
  <c:remove var="web4j_key_for_messages" scope ="session"/>
 </c:if>
</p>
