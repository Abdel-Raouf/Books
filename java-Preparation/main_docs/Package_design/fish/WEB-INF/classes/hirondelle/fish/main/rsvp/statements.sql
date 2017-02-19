RSVP_LIST_ACTIVE_MEMBERS {
 SELECT Member.Id, Member.Name, IsActive, DispositionFK
 FROM Member
 WHERE Member.IsActive = 1
 ORDER BY Name
}

-- can return 0 records, if no RSVP reponses yet made
RSVP_LIST_RESPONSES {
 SELECT Rsvp.VisitFK, Member.Id, Member.Name, Rsvp.IsAttending
 FROM Member LEFT JOIN Rsvp ON Member.Id = Rsvp.MemberFK
 WHERE 
 Member.IsActive = 1
 AND Rsvp.VisitFK = ?
}

-- fetches an existing record
RSVP_FETCH_FOR_CHANGE {
 SELECT Rsvp.VisitFK, Member.Id, Member.Name, Rsvp.IsAttending
 FROM Member, Rsvp 
 WHERE 
 Member.Id = Rsvp.MemberFK
 AND Rsvp.VisitFK = ?
 AND Rsvp.MemberFK = ?
}

RSVP_CHANGE {
  UPDATE Rsvp SET IsAttending = ? WHERE VisitFK = ? and MemberFK = ?
}

RSVP_ADD {
  INSERT INTO Rsvp (VisitFK, MemberFK, IsAttending) VALUES(?,?,?)
}

RSVP_FETCH_NUM_YES {
 SELECT COUNT(*) AS NUM_YES 
 FROM Rsvp, Member
 WHERE
 Rsvp.MemberFK = Member.Id
 AND Rsvp.VisitFK = ?
 AND Rsvp.IsAttending = 1
 AND Member.IsActive = 1
}

 
