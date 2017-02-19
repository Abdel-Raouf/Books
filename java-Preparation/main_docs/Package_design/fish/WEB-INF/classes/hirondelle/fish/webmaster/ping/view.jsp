<%-- List links to exercise basic sanity. --%>
 <P align="center"><b>SUCCESS</b> - you have successfully pinged this web application.
 
 <P>These links should be bookmarked by the webmaster :
 <P> 
 <table cellspacing="0" cellpadding="3" border="1" class="report" title="Links for testing servers." align="center">
  <tr>
   <th>Test For</th>
   <th>Link</th>
   <th>Description</th>
  </tr>
  <tr>
   <td>Is the Container running?</td>
   <td><a href="/manager/html/list">test</a></td>
   <td>Pings the server, not the web application. 
   <em>This link is specific to each Container.</em></td>
  </tr>
  <tr>
   <td>Is the Web app running?</td>
   <td>
    <c:url value="../../Ping.html" var="webAppRunningURL"/> 
    <a href='${webAppRunningURL}'>test</a>
   </td>
   <td>Serves a simple HTML page.</td>
  </tr>
  <tr>
   <td>Is the Controller running?</td>
   <td>
    <c:url value="Ping.do" var="controllerRunningURL"/> 
    <a href='${controllerRunningURL}'>test</a>
   </td>
   <td>Serves this Ping page, through the Controller.</td>
  </tr>
  <tr>
   <td>Is the database running?</td>
   <td> 
    <c:url value="../diagnostics/ShowDiagnostics.do#Database" var="databaseRunningURL"/> 
    <a href='${databaseRunningURL}'>test</a></td>
   <td>If a database is not running, no info will be shown on the Diagnostics page.</td>
  </tr>
 </table>
 
<P>
<P>
Links to each module in this application should also be bookmarked :
 <ul>
  <li><c:url value="../../main/home/HomePageAction.do" var="showHomeURL"/> <a href='${showHomeURL}'>Main</a>  
  <li><c:url value="../performance/ShowPerformance.do" var="webmasterURL"/> <a href='${webmasterURL}'>Webmaster</a>  
  <li><c:url value="../../translate/locale/SupportedLocaleAction.do?Operation=List" var="translationURL"/> <a href='${translationURL}'>Translation</a>  
  <li><c:url value="../../access/user/UserAction.do?Operation=List" var="accessURL"/> <a href='${accessURL}'>Access Control</a>  
  <li><c:url value="../../all/preferences/PreferencesAction.do?Operation=Show" var="preferencesURL"/> <a href='${preferencesURL}'>Preferences</a>  
 </ul>
