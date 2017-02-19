<%-- 
Serves as a template for all pages in the main business module, by defining their layout in one place. 
Other modules follow a similar style, but use their own template.
Parameters to this template include : contents of the <title> tag, and  the name of the JSP which forms the "body" of the page.
--%>
<%@ page contentType="text/html" %> <%-- must appear first! --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <tags:head/>
</head>
 
<body onload='showFocus()'>
<w:tooltip>  
<div align="center" class='main-title'>
  Fish And Chips Club
</div>
<P>

<div class="menu-bar">
<w:txtFlow>
 <w:highlightCurrentPage styleClass='highlight'> 
  <c:url value="/main/home/HomePageAction.show" var="homeURL"/> <A href='${homeURL}' accesskey='H'>Home</a>
  <c:url value="/main/rsvp/RsvpShow.show" var="showRsvpURL"/> <A href='${showRsvpURL}'>Rsvp</a>
  <c:url value='/main/discussion/CommentAction.show' var='viewCommentsURL'>
   <c:param name='PageIndex' value='1' />
   <c:param name='PageSize' value='10' />
  </c:url>
  <%-- Need to escape the ampersand in the link's href attribute.  --%>
   <a href='${w:ampersand(viewCommentsURL)}'>Discussion</A> 
  <c:url value="/main/visit/VisitAction.list" var="visitEditURL"/> <A HREF='${visitEditURL}'>Visits</A>
  <c:url value="/main/rating/RatingAction.list" var="ratingEditURL"/> <A HREF='${ratingEditURL}'>Ratings</A>
  <c:url value="/main/resto/RestoAction.list" var="restoActionURL"/> <A HREF='${restoActionURL}'>Restaurants</A>
  <c:url value="/main/member/MemberAction.list" var="memberEditURL"/> <A HREF='${memberEditURL}'>Members</A>
  <c:url value="/main/search/RestoSearchAction.show" var="searchRestoURL"/> <A HREF='${searchRestoURL}'>Search</A>
  <w:show ifRole="user-president"><c:url value="/main/reports/Report1Action.show" var="reportOneURL"/> <A href='${reportOneURL}'>Reports</a></w:show>
  <tags:commonMenuItems/>
 </w:highlightCurrentPage> 
 </w:txtFlow>
</div>

<%-- Display error and information messages. --%>
<tags:displayMessages/>

<div class="body">
 <c:if test="${not empty param.TBody}">
 <jsp:include page='${param.TBody}' flush="true"/>
 </c:if>
 <c:if test="${empty param.TBody}">
  <jsp:include page="Error.html" flush="true"/>
 </c:if>
</div>

<tags:footer/>

</body>
</w:tooltip>
</html>
