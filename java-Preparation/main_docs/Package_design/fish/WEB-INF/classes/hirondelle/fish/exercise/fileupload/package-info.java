/** 
Upload image files to the server.
Saves images both to the file system, and to the database.
 
<P>This feature exercises WEB4J support for file upload forms.
WEB4J itself has no API related specifically to file uploads.
The web4j data layer works with an InputStream. 
In Servlet 3.0, the servlet API itself will give you access to the uploaded file, as an InputStream.
In Servlet 2.5, you can do the same, but third-party tools are necessary.
 
 
<P>This package uses Servlet 2.5, along with the Apache Commons <a href='http://commons.apache.org/fileupload/'>File Upload</a> tool.
The generous Apache License will likely allow you to use it in your applications as well.
 
<P>It's interesting to note that even if a <em>single</em> file upload control is present in a form, 
then <em>all</em> of the form's data, including that passed by controls that are <em>not</em> file upload controls, 
are <em>not</em> available in the usual way through the Servlet 2.5 API.
 
<P>For file upload forms, the usual <tt>HttpServletRequest.getParameter(String)</tt> method does not work - for all 
parameters in the request, not just those related to file upload controls. It will return only <tt>null</tt> values.
 
<P>As a consequence, the following will not work as well (but see below):
<ul>
 <li>extracting parameter values using <tt>RequestParser</tt> methods
 <li>form population upon error, using 'recycled' request parameters
 <li>validation of param names and values using <tt>ApplicationFirewallImpl</tt>
 <li>building Model Objects using <tt>ModelBuilder</tt>
 <li>logging of request parameters by the <tt>Controller</tt>
 <li><tt>ActionTemplateXXX</tt> classes that depend on an <tt>Operation</tt> parameter
</ul>
 
<P><span class='highlight'>However, it's easy to render these items functional for file upload forms by using a wrapped 
request and a simple filter.</span> That technique is used here.
 
<P>See this <a href='http://www.javaworld.com/jw-06-2001/jw-0622-filters.html?page=5'>Java World article</a>
for further information.

<p><span class='highlight'>It's important to note that most browsers (all?) do not respect the <tt>value</tt> attribute for 
file upload controls.</span> As a result, this makes it impossible for web4j (or any similar tool) to pre-populate such controls dynamically. 
In the case of an 'update' operation, the web4j mechanism for pre-populating the data in a file upload control doesn't work.
So, any update operations for binary data needs to be handled separately. (One option is to use only add and delete operations, 
with no update at all.)  
*/
package hirondelle.fish.exercise.fileupload;
