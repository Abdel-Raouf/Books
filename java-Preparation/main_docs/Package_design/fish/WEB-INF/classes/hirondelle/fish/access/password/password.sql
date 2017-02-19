-- When added or reset, the User has a fixed, inconvenient password, which  
-- they are encouraged to change. This is not enforced by the application, however.

-- Available only to access-control folk.
-- Changes the password to a fixed value, known to both the access control folk and the user. 
ACCESS_CONTROL.RESET_PASSWORD  {
 UPDATE Users Set Password=? WHERE Name=? 
}

-- Available to all users. All users must know their old password in order to change it.
ACCESS_CONTROL.CHANGE_PASSWORD {
  UPDATE Users Set Password=? WHERE Name=? AND Password=?
}

