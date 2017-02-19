LIST_RESTOS {
 SELECT Id, Name, Location, Price, Comment
 FROM Resto
 ORDER BY Name
}

FETCH_RESTO {
 SELECT Id, Name, Location, Price, Comment
 FROM Resto
 WHERE Id =? 
}

ADD_RESTO  {
  -- Id is an autoincrement field, populated automagically by the database.
 INSERT INTO Resto (Name, Location, Price, Comment) VALUES (?,?,?,?)
}

CHANGE_RESTO {
  UPDATE Resto SET Name=?, Location=?, Price=?, Comment=? WHERE Id=?
}

DELETE_RESTO {
  DELETE FROM RESTO WHERE Id=?
}