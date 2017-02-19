-- All Roles of a given User. The User may have no Roles at all.
-- Must use a LEFT JOIN to return Users with 0 Roles.
ACCESS_CONTROL.ROLES_FETCH {
  SELECT Users.Name, UserRole.Role
  FROM Users LEFT JOIN UserRole ON Users.Name = UserRole.Name
  WHERE Users.Name = ?
  ORDER BY UserRole.Role
}

-- Listing of all Users, with all of their 0..N Roles. 
-- This is an example of SELECTing across a relation. The corresponding Model Object 
-- has a constructor that takes a Collection for the 0..N Roles attached to the User.
-- Must use a LEFT JOIN to return Users with 0 Roles.
ACCESS_CONTROL.ROLES_LIST {
  SELECT Users.Name, UserRole.Role
  FROM Users LEFT JOIN UserRole ON Users.Name = UserRole.Name
  ORDER BY Users.Name, UserRole.Role
}

-- Here, *all* edit operations on Roles are DELETE ALL + ADD ALL Operations, 
-- executed in a transaction. This simplifies logic a great deal,
-- since there is no need to detect if items already exist. (This is 
-- possible only since no items in other tables link to a Role.)

-- DELETE all Roles for a User
ACCESS_CONTROL.ROLES_DELETE {
  DELETE FROM UserRole WHERE Name=?
}

-- INSERT a new Role for a User, one at a time
ACCESS_CONTROL.ROLES_ADD {
  INSERT INTO UserRole (Name, Role) VALUES (?,?)
}


