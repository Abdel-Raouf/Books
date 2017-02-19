LIST_SPENDING {
 SELECT Id, DatePaid, Amount, KWH,  IsEstimated, FacilityFK, Comment
 FROM Spending
 ORDER BY DatePaid DESC
}

FETCH_SPENDING {
 SELECT Id, DatePaid, Amount, KWH, IsEstimated, FacilityFK, Comment
 FROM Spending
 WHERE Id =? 
}

ADD_SPENDING  {
  -- Id is an autoincrement field, populated automagically by the database.
 INSERT INTO Spending (DatePaid, Amount, KWH, IsEstimated, FacilityFK, Comment) VALUES (?,?,?,?,?,?)
}

CHANGE_SPENDING {
  UPDATE Spending SET DatePaid=?, Amount=?, KWH=?, IsEstimated=?, FacilityFK=?, Comment=? WHERE Id=?
}

DELETE_SPENDING {
  DELETE FROM Spending WHERE Id=?
}