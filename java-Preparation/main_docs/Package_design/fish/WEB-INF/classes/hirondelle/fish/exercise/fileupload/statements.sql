ADD_IMAGE { 
  INSERT INTO Image(Description, Name, MimeType, Length, Img) VALUES (?,?,?,?,?)
}

DELETE_IMAGE {
 DELETE FROM Image WHERE Id=?
}

LIST_IMAGES {
 SELECT Id, Description, Name, MimeType, Length, Img FROM Image
}

FETCH_IMAGE {
 SELECT Id, Description, Name, MimeType, Length, Img FROM Image WHERE Id=?
}


