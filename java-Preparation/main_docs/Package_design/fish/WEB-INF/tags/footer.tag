<%-- Tag file: footer. --%>
<%@ include file="/WEB-INF/TagHeader.jspf" %>
<div class="legalese" >
  <jsp:useBean id="now" class="java.util.Date" />
  <w:txt>Copyright</w:txt> &copy; <w:showDate name="now" pattern="yyyy" />
  ${web4j_key_for_app_info.author} - <span title="Build Date : ${web4j_key_for_app_info.buildDate}">${web4j_key_for_app_info.name}/${web4j_key_for_app_info.version}</span>
  - <a href="${web4j_key_for_app_info.link}">WEB4J</a>
<w:help>-<span class="help" title='Validate - Requires turning off login security!'><a href='http://www.web4j.com/fish/javadoc/index.html' title='Documentation'>Javadoc</a>
  - <a href="http://jigsaw.w3.org/css-validator/check/referer" >CSS</a>
  - <a href="http://validator.w3.org/check/referer" >HTML</a>
   </span class="help">   
</w:help>
</div>
