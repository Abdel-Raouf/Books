<%-- Exercise multi-valued parameters. --%>

<w:help>
 <div class="help">
  <w:txt wikiMarkup="true" locale="en">
   This page is artificial. It does not edit the database in any way.
   It exists only to exercise multi-valued request parameters.
  </w:txt>
  <w:txt wikiMarkup="true" locale="fr">
   [A Faire]
  </w:txt>
 </div>
</w:help>

<P>
<c:url value='ToppingsAction.apply' var='baseURL'/>
<w:populate using='itemForEdit'>
 <form action='${baseURL}' class="user-input" method='POST' name='giveMeFocus'>
 <table align="center">
 <tr>
 <td>
   <label>Pizza Toppings</label><BR>
   <input type="checkbox" name="Pizza Toppings" value="Anchovies">Anchovies<br>
   <input type="checkbox" name="Pizza Toppings" value="Salami">Salami<br>
   <input type="checkbox" name="Pizza Toppings" value="Green Pepper">Green Pepper<br>
   <input type="checkbox" name="Pizza Toppings" value="Bacon">Bacon<br>
   <input type="checkbox" name="Pizza Toppings" value="Hamburger">Hamburger<br>
   <input type="checkbox" name="Pizza Toppings" value="Toasted Dolphin">Toasted Dolphin<br>
 </td>
 <td>
  <label>Artists</label><BR>
  <select name="Artists" multiple>
    <option>Tom Thomson</option>
    <option>Mary Pratt</option>
    <option>Paul-Emile Borduas</option> 
    <option>Arthur Lismer</option>
    <option>Alex Janvier</option>
  </select>
 </td>
 </tr>
 <tr>
  <td>
   <label>Ages</label><BR>
   <input type="checkbox" name="Age" value="10">10
   <input type="checkbox" name="Age" value="20">20<br>
   <input type="checkbox" name="Age" value="30">30
   <input type="checkbox" name="Age" value="40">40<br>
   <input type="checkbox" name="Age" value="50">50 
   <input type="checkbox" name="Age" value="60">60<br>
   <input type="checkbox" name="Age" value="70">70
   <input type="checkbox" name="Age" value="80">80<br>
   <input type="checkbox" name="Age" value="90">90
   <input type="checkbox" name="Age" value="100">100<br>
  </td>
  <td>
   <label>Salaries</label><BR>
   <input type="checkbox" name="Desired Salary" value="10000.00">$10,000
   <input type="checkbox" name="Desired Salary" value="20000.00">$20,000<br>
   <input type="checkbox" name="Desired Salary" value="30000.00">$30,000
   <input type="checkbox" name="Desired Salary" value="40000.00">$40,000<br>
   <input type="checkbox" name="Desired Salary" value="50000.00">$50,000
   <input type="checkbox" name="Desired Salary" value="60000.00">$60,000<br>
   <input type="checkbox" name="Desired Salary" value="70000.00">$70,000
   <input type="checkbox" name="Desired Salary" value="80000.00">$80,000<br>
   <input type="checkbox" name="Desired Salary" value="90000.00">$90,000
   <input type="checkbox" name="Desired Salary" value="100000.00">$100,000<br>
  </td>
 </tr>
 <tr>
 <td>
 <label>Dates</label><BR>
  <select name="Birth Date" multiple>
    <option>12/31/1969</option>
    <option>01/01/1970</option>
    <option>01/01/2000</option>
  </select>
 </td>
 <td>&nbsp;</td>
</tr>
 <tr>
  <td align="center" colspan=2>
   <input type="submit" value="Submit Multivalued Params">
  </td>
 </tr>
 </table>
 </form>
 </w:populate>

 
