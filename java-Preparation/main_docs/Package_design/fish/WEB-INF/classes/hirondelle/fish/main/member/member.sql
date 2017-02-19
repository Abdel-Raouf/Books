MEMBER_FETCH {
  SELECT Id, Name, IsActive, DispositionFK  FROM Member WHERE Id=?
}

MEMBER_LIST {
  SELECT Id, Name, IsActive, DispositionFK FROM Member ORDER BY Name
}

MEMBER_ADD {
  INSERT INTO Member(Name, IsActive, DispositionFK) VALUES (?,?,?)
}

MEMBER_CHANGE {
  UPDATE Member SET Name=?, IsActive=?, DispositionFK=? WHERE Id=?
}

MEMBER_DELETE {
  DELETE FROM Member WHERE Id=?
}

COUNT_ACTIVE_MEMBERS {
  SELECT COUNT(*) FROM Member WHERE IsActive=1
}