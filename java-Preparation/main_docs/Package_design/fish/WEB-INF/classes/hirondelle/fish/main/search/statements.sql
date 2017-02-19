-- This feature uses the DynamicCriteria class to create the body of the WHERE and ORDER BY clauses.
--
-- The DynamicCriteria class requires that any JOINs or *static* WHERE criteria must appear here, in the 
-- .sql file. Only *dynamic* WHERE criteria (taking parameters) can be appended in code with
-- the DynamicCriteria class. 
--
RESTO_DYNAMIC_SEARCH {
  SELECT Id, Name, Location, Price, Comment
  FROM Resto
  -- Dynamic items appear here
  -- Different combinations are possible
  -- WHERE Name Like ?
  -- AND Price <= ? AND Price >= ?
  -- ORDER BY Price DESC
}
