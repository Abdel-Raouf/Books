ADD_COMMENT  {
 INSERT INTO Discussion (Name, Body, CreationDate) VALUES (?,?,?)
}

-- Any number of 'constants' blocks can be defined, anywhere 
-- in the file. Such constants must be defined before being
-- referenced in a SQL statement, however.
constants {
  num_messages_to_view = 25
}

-- Example of referring to a constant defined above.
FETCH_RECENT_COMMENTS {
 SELECT Name, Body, CreationDate 
 FROM Discussion 
 ORDER BY Id DESC LIMIT ${num_messages_to_view}
}

