constants {
  true = 1
}

-- ORDER BY clauses are generated dynamically from request parameters

-- Number of Visits for each Restaurant
VISITS_PER_RESTO {
  SELECT Resto.Name AS "Name", Count(*) as "Visits"
  FROM Resto, Visit
  WHERE Visit.RestoFK = Resto.Id
  GROUP BY Resto.Name
}

-- Number of Visits for each Member
VISITS_PER_MEMBER {
  SELECT Member.Name AS "Name", Count(*) as "Visits"
  FROM Rsvp, Member
  WHERE Rsvp.MemberFK = Member.Id
  AND Rsvp.IsAttending = ${true}
  GROUP BY Member.Name
}

-- Number of Members who RSVPed 'yes' to the 10 most recent visits
NUM_MEMBERS_FOR_RECENT_VISITS {
  SELECT Visit.LunchDate AS "Date", Count(*) as "Members"
  FROM Visit, Rsvp
  WHERE Rsvp.VisitFK = Visit.Id
  AND Rsvp.IsAttending = ${true}
  GROUP BY Visit.LunchDate
}

PARTICIPATION_PER_YEAR {
  SELECT YEAR(Visit.LunchDate) AS "Year", Count(*) as "Attendance"
  FROM Visit, Rsvp
  WHERE Rsvp.VisitFK = Visit.Id
  AND Rsvp.IsAttending = ${true}
  GROUP BY YEAR(Visit.LunchDate)
}

TOTAL_ATTENDANCE {
  SELECT COUNT(0) as "TotalAttendance"
  FROM Visit, Rsvp
  WHERE Rsvp.VisitFK = Visit.Id
  AND Rsvp.IsAttending = ${true}
}