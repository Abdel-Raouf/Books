package hirondelle.electricity.codes;

import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.Code;

/*** All code tables used in this application.
* 
* <P>See package summary for important information.
*/
public enum CodeTable { 

  /** Residential, commercial, and so on */ 
  FACILITY("facilities");
  
  /** 
  * Return the name of the code table. 
  * This name is used as the key for the corresponding {@code List<Code>} 
  * placed in application scope.
  */
  public Id getTableName() { return fTableName; }
  
  /**
  * Return a fully populated {@link Code}, given its {@link Id} and {@link CodeTable}.
  * 
  * <P><span class='highlight'>Called from Model Object constructors that wish to translate a 
  * simple {@link Id} for a known code table into a fully populated {@link Code}</span>, complete 
  * with user-presentable text. See {@link CodeTableUtil} for more information.
  */
  public static Code codeFor(Id aCodeId, CodeTable aCodeTable){
    return CodeTableUtil.populate(aCodeId, aCodeTable);
  }
  
  // PRIVATE //
  private CodeTable(String aTableName){
    fTableName = new Id(aTableName);
  }
  private final Id fTableName;
}
