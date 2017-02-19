/** 
Code Tables for the application.

 <P><span class='highlight'>The general approach in this implementation is to treat the underlying 
 id (the primary key) of the {@link hirondelle.web4j.model.Code} as being most important, and all 
 other items as secondary aliases for the id - the text, short form, and long form.</span>
  (Some would object to this style, since it makes underlying database identifiers more visible 
  than usually desired. In practice, however, this does not seem to be a significant issue.)
 
<h3>Startup</h3>
 Upon startup, this application's implementation of {@link hirondelle.web4j.StartupTasks} will call 
 {@link hirondelle.fish.main.codes.CodeTableUtil#init(ServletContext)} to initialize all items 
 related to Code Tables. This same method must also be called if any code table changes content.

 <h3>HTML Forms</h3>
 The code tables fetched during startup are referenced <em>directly</em> in HTML forms, as application scope objects, using 
{@link hirondelle.fish.main.codes.CodeTable#getTableName()} as key. 
 Typically, two aspects of a <tt>Code</tt> are referenced in a form : one of the code's textual aliases is   
 made visible to the user, <em>while its underlying id is always used as the value actually POSTed during form submission</em>.
 
 <P>Here is an example which references a code table named <tt>'dispositions'</tt> :<br>
<PRE>
  &lt;select name="Disposition"&gt;
   &lt;c:forEach var="item" items="${dispositions}"&gt;
    &lt;option value="${item.id}"&gt;${item}&lt;/option&gt;
   &lt;/c:forEach&gt;
  &lt;/select&gt;
</PRE>

 <h3>Avoid Double Escaping of Codes</h3>
 The various translation tags used in multilingual apps always escape special characters as part of their implementation.
 If the above snippet appears withing a translation tag then it should be changed slightly to : 
<PRE>
&lt;w:txtFlow&gt;
  ...
  &lt;select name="Disposition"&gt;
   &lt;c:forEach var="item" items="${dispositions}"&gt;
    &lt;option value="${item.id}"&gt;${item.text.rawString}&lt;/option&gt;
   &lt;/c:forEach&gt;
  &lt;/select&gt;
  ...
&lt;/w:txtFlow&gt;

<P>It is easy to forget to perform the above, especially when special characters appear infrequently.
This is a pitfall of using the <tt>Code</tt> class. This pitfall appears only with multilingual apps, 
and when your codes includes special characters.
See <tt>EscapeChars.forHTML()</tt> for the list of escaped characters. 
</PRE>

This will avoid double-escaping of special characters, which is always undesirable.

 <h3>Model Objects</h3> 
 <span class="highlight">In Model Objects, the constructor takes a single {@link hirondelle.web4j.model.Id} for the 
 code - not the full {@link hirondelle.web4j.model.Code} object. Inside the constructor, the <tt>Id</tt> is 'expanded' into a 
 full <tt>Code</tt> object, using {@link hirondelle.fish.main.codes.CodeTable#codeFor(Id, CodeTable)}.</span>  
 
 <h3>Consequences</h3>
 <P>This style has these consequences :
<ul>
 <li>there is no need to repeatedly query the database for simple data which rarely changes (the content of the code table).
 <li><tt>Action</tt> classes usually do not need to place code tables into request scope, since they are already present 
 in application scope.
 <li>the regular, short and long forms of a {@link hirondelle.web4j.model.Code} are always available, if needed.
 <li>underlying <tt>SELECT</tt> queries for Model Objects do not need to use joins for code tables, 
 since the lookup is performed in the Model Object constructor.  
 <li>logging of the Model Object will display the user-friendly text, not the id.
 <li>there is a drawback concerning translation and ordering of codes : if codes need to be translated according to  
 the user's <tt>Locale</tt>, then there is an issue with alphabetical ordering. 
 <em>If the ordering is alphabetical</em>, then reordering of codes should be done for each <tt>Locale</tt>, according 
 to the user-visible text applicable to that <tt>Locale</tt>. However, there is no explicit provision for 
 this in the implementation described here. A workaround is to simply create a code table in an <tt>Action</tt> and place it 
 in request scope, to reflect the desired text and order. (This is always possible.) 
</ul>
*/
package hirondelle.fish.main.codes;
