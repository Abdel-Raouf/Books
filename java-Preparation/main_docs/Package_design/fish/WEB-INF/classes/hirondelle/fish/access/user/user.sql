-- The password is returned here, but it is not displayed in the 
-- listing. Including it here allows the Model Object to perform checks.
ACCESS_CONTROL.USER_LIST {
 SELECT Name, Password
 FROM Users
 ORDER BY Name
}

ACCESS_CONTROL.USER_ADD  {
 INSERT INTO Users (Name, Password) VALUES (?,?)
}

-- Called only after all Roles have been deleted.
ACCESS_CONTROL.USER_DELETE {
  DELETE FROM Users WHERE Name=?
}

