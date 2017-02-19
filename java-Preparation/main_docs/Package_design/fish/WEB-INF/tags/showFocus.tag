 <%-- Easier user input : show initial focus on first link or form of a conventional name. --%>
 <script language='javascript' type='text/javascript'>
  //give focus either to first non-hidden control in a form having a conventional name, or first link in page
  function showFocus(){
    if ( document.forms['giveMeFocus'] != null ){
      var theForm = document.forms['giveMeFocus'];
      for ( idx = 0; idx < theForm.elements.length; idx++){
        if (theForm.elements[idx].type != null && theForm.elements[idx].type != 'hidden') {
          theForm.elements[idx].focus();
          break;       
        }
      }
    }
    else if ( document.links.length > 0 ) {
      document.links[0].focus();
    }
  }
 </script>