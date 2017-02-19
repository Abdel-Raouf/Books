<%-- 
Display error and information messages. 
If a JSP needs to display a dynamic message to the user, it may 
include this tag file. 

Some might choose to include this tag file in the Template.jsp, while 
others would prefer to include it in any page that needed it.
--%>
<%@ include file="/WEB-INF/TagHeader.jspf" %>

<%--
These items use AppResponseMessages.getMessage()
which uses settings in web.xml to format any dates, numbers, and so on, that may appear 
in the message. If this formatting policy is undesired, one may use always other methods
in AppResponseMessage to directly access the underlying unformatted data. 
--%>

<p class="display-messages">

 <c:if test="${not empty web4j_key_for_messages}"> 
  <w:messages name="web4j_key_for_messages">
   <span class="message">placeholder</span><br>
  </w:messages>
  <c:remove var="web4j_key_for_messages" scope ="session"/>
 </c:if>

 <c:if test="${not empty web4j_key_for_errors}"> 
  <span class="error">Oops!</span>
  <w:messages name="web4j_key_for_errors">
   <span class="error">placeholder</span><br>
  </w:messages>
  <c:remove var="web4j_key_for_errors" scope ="session"/>
 </c:if>

</p>
