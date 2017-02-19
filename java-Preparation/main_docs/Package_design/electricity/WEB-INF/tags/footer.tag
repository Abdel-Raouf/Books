<%-- Footer. --%>
<%@ include file="/WEB-INF/TagHeader.jspf" %>
<div class="legalese" >
  <jsp:useBean id="now" class="java.util.Date" />
  Copyright &copy; <w:showDate name="now" pattern="yyyy" /> 
  ${web4j_key_for_app_info.author} - <span title="Build Date : ${web4j_key_for_app_info.buildDate}">${web4j_key_for_app_info.name}/${web4j_key_for_app_info.version}</span>
</div>
