-- Here, the boolean FALSE is added to the ResultSet only so that 
-- the BaseText Model Object can be used to parse the result.
-- This avoids the need to build a Model Object just for this query.
TRANSLATION.UNKNOWN_LIST {
 SELECT Id, Text, 'FALSE' FROM UnknownBaseText ORDER BY Text;
}

TRANSLATION.UNKNOWN_ADD {
 INSERT INTO UnknownBaseText (Text) VALUES(?) 
}

TRANSLATION.UNKNOWN_DELETE {
 DELETE FROM UnknownBaseText WHERE Text=?
}

TRANSLATION.UNKNOWN_DELETE_ALL {
 DELETE FROM UnknownBaseText;
}

TRANSLATION.UNKNOWN_COUNT {
 SELECT COUNT(*) FROM UnknownBaseText;
}
