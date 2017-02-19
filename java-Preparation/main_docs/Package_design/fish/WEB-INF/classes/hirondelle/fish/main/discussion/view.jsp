<%-- List and add general comments.  --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
  Simple message board for any comments people may have. Has simple paging mechanism.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
  [A Faire.]
  </w:txt>
 </div>
</w:help>

<c:url value="CommentAction.apply" var="baseURL"> 
 <c:param name='PageIndex' value='1' />
 <c:param name='PageSize'   value='10' />
</c:url>
<w:populate>
 <form action='${w:ampersand(baseURL)}' method='POST' class="user-input" >
  <w:txtFlow>
   <table align="center">
    <tr>
     <td>
      <label>Comment</label> *
     </td>
    </tr>
    <tr>
     <td>
      <textarea name="Comment Body" rows=3 cols=65>
      </textarea>
     </td>
    </tr>
  </w:txtFlow>
    <tr>
     <td align="center" colspan=2>
      <input type="submit" value="Post Comment"> 
     </td>
    </tr>
   </table>
 </form>
 </w:populate>

<P>

<%-- Listing of recently posted comments. --%>
<w:txtFlow>
 <tags:pagerLinks/>
 <table class='report' title="Comments" align="center" width="90%"> 
  <caption>Comments</caption>
</w:txtFlow>
<w:alternatingRow> 
<c:forEach var="comment" items="${itemsForListing}" begin="${param.PageSize * (param.PageIndex-1)}" end="${param.PageSize * param.PageIndex - 1}">
   <tr class="row_highlight">
    <td>
     <c:set value="${comment.date}" var="commentDate"/>
     [<w:showDate name="commentDate" pattern="E, MMM dd"/> <b>${comment.userName}</b>] :
     <%-- Here, we allow wiki formatting, while escaping special HTML chars. --%>
     <%-- The w:txt tag does the formatting/escaping, so we need the *unescaped* text from SafeText.getRawString(). --%>
     <w:txt translate="false" wikiMarkup="true">
      ${comment.body.rawString} 
     </w:txt>
    </td>
   </tr>
 </c:forEach>
</w:alternatingRow>
 </table>






