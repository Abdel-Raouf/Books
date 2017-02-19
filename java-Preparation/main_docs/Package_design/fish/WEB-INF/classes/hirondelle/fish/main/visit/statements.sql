VISIT_FETCH {
  SELECT Visit.Id, RestoFK, LunchDate, Message FROM Visit WHERE Visit.Id=? 
}

VISIT_FETCH_NEXT_FUTURE {
  SELECT Visit.Id, RestoFK, LunchDate, Message FROM Visit WHERE LunchDate >= CURRENT_DATE - 1 ORDER BY LunchDate ASC;
}

-- The ORDER BY clause is added dynamically to this base statement
-- The join to Resto is needed if the sort is on Resto.Name; otherwise, it is redundant but harmless.
VISIT_LIST {
  SELECT Visit.Id, RestoFK, LunchDate, Message
  FROM Visit INNER JOIN Resto ON RestoFK = Resto.Id
}

VISIT_ADD {
  INSERT INTO Visit(RestoFK, LunchDate, Message) VALUES (?,?,?)
}

VISIT_CHANGE {
  UPDATE Visit SET RestoFK=?, LunchDate=?, Message=? WHERE Id=?
}

VISIT_DELETE {
  DELETE FROM Visit WHERE Id=?
}

