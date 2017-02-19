package hirondelle.fish.main.codes;

import java.util.*;
import java.util.logging.*;
import javax.servlet.ServletContext;

import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.model.Code;
import hirondelle.web4j.security.SafeText;

/**
 Fetches code tables from the database upon startup.
 
 <P>Operations performed : <a href='code_table.sql'>SQL statements</a>.
 
 <P>See package summary for important information.
*/
public final class CodeTableUtil {

  public static final SqlId FETCH_RESTO_CODES =  new SqlId("FETCH_RESTO_CODES");
  public static final SqlId FETCH_LOCALE_CODES =  new SqlId("TRANSLATION", "FETCH_LOCALE_CODES");
  public static final SqlId FETCH_DISPOSITION_CODES =  new SqlId("FETCH_DISPOSITION_CODES");
  
  /**
   Called by the application's implementation of {@link hirondelle.web4j.StartupTasks}, and whenever the 
   content of a code table changes.
   
   <P>Performs the following :
   <ul>
   <li>fetch all code tables from the database
   <li>store all code tables internally, such that any trivial translations from {@link Id} to {@link Code} 
   can be performed by this class
   <li>place all code tables in application scope, under the keys defined by 
   {@link CodeTable#getTableName()}, as a {@code List<Code>}. This will allow JSPs to reference code 
   tables directly by name, without any help from an associated <tt>Action</tt>. (Some might prefer a 
   <tt>Set</tt> instead of a <tt>List</tt>, but a <tt>Set</tt> would have the disadvantage of hiding any
   duplicate items.)
   </ul>
  */
  public static void init(ServletContext aContext) throws DAOException {
    fLogger.fine("Fetching code tables.");
    fContext = aContext;
    fAllCodeTables = new LinkedHashMap<CodeTable, Map<Id, Code>>();
    //this cycling ensures all items in the CodeTable enum are processed
    for(CodeTable codeTable: CodeTable.values()){
      fetchAndRememberCodeTable(codeTable);
      placeInAppScope(codeTable);
    }
  }
  
  /**
   Return a fully populated {@link Code}, given its {@link Id} and {@link CodeTable}.
   
   <P>Called from Model Objects constructors that need to translate a simple {@link Id} for a known 
   code table into a fully populated {@link Code}.
   
   <P>[Bug Fix Sep 10 2007]<br>
   If <tt>aCodeId</tt> is itself <tt>null</tt>, then <tt>null</tt> is returned. 
    
   <P><em>Design Note:</em><br>
   This item is package-private. The {@link CodeTable#codeFor(Id, CodeTable)} method is a <tt>public</tt> 
   version of this method, and simply does a call forward to this one. The reason is simply to reduce the number 
   of imports needed in Model Objects that use Codes from 3 to 2 : {@link Code} and {@link CodeTable}.
  */
  static Code populate(Id aCodeId, CodeTable aCodeTable){
    Code result = null;
    Map<Id, Code> codeTable = fAllCodeTables.get(aCodeTable);
    if ( codeTable == null ){
      throw new RuntimeException("Cannot populate item. Unknown code table " + Util.quote(aCodeTable));
    }
    if ( aCodeId != null ){
      result = codeTable.get(aCodeId);
      if ( result == null ){
        throw new RuntimeException("Cannot find item in code table " + Util.quote(aCodeTable) + " using Id: " + Util.quote(aCodeId));
      }
    }
    return result;
  }

  // PRIVATE //
  
  /**
   Table for translating a given table/id into a fully populated Code object.
   Populated upon startup.
  */
  private static Map<CodeTable, Map<Id, Code>> fAllCodeTables;
  
  private static ServletContext fContext;
  
  private static final Logger fLogger = Util.getLogger(CodeTableUtil.class);
  
  private static void fetchAndRememberCodeTable(CodeTable aCodeTable) throws DAOException {
    Map<Id, Code> codeTable = null;
    if ( CodeTable.DISPOSITIONS == aCodeTable ){
      codeTable = getDispositions();
    }
    else if( CodeTable.SUPPORTED_LOCALES == aCodeTable){
      codeTable = getLocales();
    }
    else if( CodeTable.RESTAURANTS == aCodeTable ){
      codeTable = getRestoCodes();
    }  
    else if ( CodeTable.RATINGS == aCodeTable ){
      codeTable = getRatings();
    }
    else {
      throw new AssertionError("CodeTable not known to CodeTableUtil : " + aCodeTable);
    }
    fLogger.fine("Code Table " + Util.quote(aCodeTable) + ": " + Util.logOnePerLine(codeTable));
    fAllCodeTables.put(aCodeTable, codeTable);
  }
  
  /** 
   Example of the most typical form of code table - from a small database table created 
   solely to define this code table.
  */ 
  private static Map<Id, Code> getDispositions() throws DAOException {
    Map<Id, Code> result = new LinkedHashMap<Id, Code>();
    List<Code> codes = Db.list(Code.class, FETCH_DISPOSITION_CODES);
    result = Util.asMap(codes, Id.class, "getId");
    return result;
  }

  /** 
   Example of a nearly typical form of code table - from a small database table created 
   solely to define this code table. This item is a bit unusual since Locale has special 
   standing as a building block object in WEB4J.
  */ 
  private static Map<Id, Code> getLocales() throws DAOException {
    Map<Id, Code> result = new LinkedHashMap<Id, Code>();
    List<Code> codes = Db.list(Code.class, FETCH_LOCALE_CODES);
    result = Util.asMap(codes, Id.class, "getId");
    return result;
  }

  /** Example of a code table created from a domain item used elsewhere in other, wider contexts.  */
  private static Map<Id, Code> getRestoCodes() throws DAOException {
    Map<Id, Code> result = new LinkedHashMap<Id, Code>();
    List<Code> codes = Db.list(Code.class, FETCH_RESTO_CODES);
    result = Util.asMap(codes, Id.class, "getId");
    return result;
  }
  
  /** 
   Unusual example of a simple code table (the numbers 1..10) created entirely in memory, 
   and not fetched from the database at all. This style should be used with care.   
  */
  private static Map<Id, Code> getRatings() throws DAOException {
    Map<Id, Code> result = new LinkedHashMap<Id, Code>();
    for(int idx=0; idx <= 10; ++idx ){
      Id id  = new Id(new Integer(idx).toString());
      Code code = null;
      try {
        code = new Code(id, SafeText.from(id.toString()));
      }
      catch (ModelCtorException ex){
        throw new DAOException("Cannot build Code in expected manner.",ex);      
      }
      result.put(id, code);
    }
    return result;
  }
  
  private static void placeInAppScope(CodeTable aCodeTable){
    Map<Id, Code> codeTable = fAllCodeTables.get(aCodeTable);
    Collection<Code> codeValues = codeTable.values();
    fContext.setAttribute(aCodeTable.getTableName().toString(), codeValues);
  }
}
