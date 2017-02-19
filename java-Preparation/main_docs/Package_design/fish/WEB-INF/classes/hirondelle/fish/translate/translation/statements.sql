constants {
 false = 0
 true = 1
 base_locale = en
}

--
-- Fetching Translations 
--
TRANSLATION.FETCH_TRANSLATIONS_FROM_CODER_KEYS {
SELECT 
 BaseText.Text AS "BaseText", 
 Locale.ShortForm AS "Locale", 
 Translation.Text AS "Translation"
FROM 
 Translation 
 INNER JOIN BaseText ON Translation.BaseTextFK = BaseText.Id
 INNER JOIN Locale ON Translation.LocaleFK = Locale.Id
WHERE
 BaseText.IsCoderKey = ${true}
ORDER BY 
 BaseText.Text, Locale.ShortForm;
}

TRANSLATION.FETCH_TRANSLATIONS_FROM_NATURAL_LANGUAGE_KEYS_ONE {
SELECT 
 BaseText.Text AS "BaseText", 
 '${base_locale}' AS "Locale", 
 BaseText.Text AS "Translation"
FROM 
 BaseText
WHERE
 BaseText.IsCoderKey = ${false}
ORDER BY 
 "BaseText", "Locale";
}

-- Must be UNIONed with previous *_ONE query
TRANSLATION.FETCH_TRANSLATIONS_FROM_NATURAL_LANGUAGE_KEYS_TWO {
SELECT 
 BaseText.Text AS "BaseText", 
 Locale.ShortForm AS "Locale", 
 Translation.Text AS "Translation"
FROM 
 Translation 
 INNER JOIN BaseText ON Translation.BaseTextFK = BaseText.Id
 INNER JOIN Locale ON Translation.LocaleFK = Locale.Id
WHERE
 BaseText.IsCoderKey = ${false} AND
 Locale.ShortForm <> '${base_locale}'
ORDER BY 
 "BaseText", "Locale";
}

--
-- Editing Translations :
-- 

TRANSLATION.LIST {
SELECT 
   BaseText.Text AS "BaseText", 
   Locale.ShortForm AS "Locale",
   Translation.Text AS "Translation",
   Translation.BaseTextFK AS "BaseTextId",
   Translation.LocaleFK AS "LocaleId"
 FROM 
  Translation 
  INNER JOIN BaseText ON Translation.BaseTextFK = BaseText.Id
  INNER JOIN Locale ON Translation.LocaleFK = Locale.Id
 WHERE
  BaseText.Id = ?
 ORDER BY 
  Locale.ShortForm;
}

TRANSLATION.FETCH {
SELECT 
 BaseText.Text AS "BaseText", 
 Locale.ShortForm AS "Locale", 
 Translation.Text AS "Translation",
 Translation.BaseTextFK,
 Translation.LocaleFK
FROM 
 Translation 
 INNER JOIN BaseText ON Translation.BaseTextFK = BaseText.Id
 INNER JOIN Locale ON Translation.LocaleFK = Locale.Id
WHERE
 BaseTextFK = ? AND 
 LocaleFK = ?;
}


TRANSLATION.ADD {
  INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(?,?,?);
}

TRANSLATION.CHANGE {
  UPDATE Translation SET Text = ? WHERE BaseTextFK=? AND LocaleFK=?;
}

TRANSLATION.DELETE {
  DELETE FROM Translation WHERE BaseTextFK=? AND LocaleFK=?;
}
