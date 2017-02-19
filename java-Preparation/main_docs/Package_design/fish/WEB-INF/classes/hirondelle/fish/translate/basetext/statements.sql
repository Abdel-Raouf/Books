TRANSLATION.BASE_TEXT_LIST {
 SELECT Id, Text, IsCoderKey FROM BaseText ORDER BY Text;
}

TRANSLATION.BASE_TEXT_FETCH {
 SELECT Id, Text, IsCoderKey FROM BaseText WHERE Id=?
}

TRANSLATION.BASE_TEXT_ADD {
 INSERT INTO BaseText (Text, IsCoderKey) VALUES(?,?) 
}

TRANSLATION.BASE_TEXT_CHANGE {
 UPDATE BaseText SET Text=?, IsCoderKey=? WHERE Id=? 
}

TRANSLATION.BASE_TEXT_DELETE {
 DELETE FROM BaseText WHERE Id=?
}
