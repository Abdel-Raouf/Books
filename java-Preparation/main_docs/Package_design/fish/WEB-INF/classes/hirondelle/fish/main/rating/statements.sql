-- There is some repetition here of the overall rating formula. 
-- Some would implement that calc in the java Model Object.
-- However, this style allows the formula to be altered here, if necessary, without a redeploy of code.

RATING_LIST {
 SELECT 
 Id, Name, FishRating, ChipsRating, PriceRating, 
 LocationRating, ServiceRating, BeerRating, ROUND(((3*FishRating + 3*ChipsRating + 3*PriceRating + 2*ServiceRating + 2*LocationRating + 1*BeerRating)/14),1) AS OverallRating
 FROM Resto
 ORDER BY OverallRating DESC
}


RATING_FETCH_FOR_CHANGE {
 SELECT 
 Id, Name, FishRating, ChipsRating, PriceRating, 
 LocationRating, ServiceRating, BeerRating, ROUND(((3*FishRating + 3*ChipsRating + 3*PriceRating + 2*ServiceRating + 2*LocationRating + 1*BeerRating)/14),1) AS OverallRating
 FROM Resto
 WHERE Id=?
}

RATING_CHANGE {
 UPDATE Resto SET
 FishRating=?, ChipsRating=?, PriceRating=?, LocationRating=?, ServiceRating=?, BeerRating=?
 WHERE Id=?
}


