# This is a MySQL script for creating 3 databases, their tables, and populating 
# them with informal test data. Tested with version 5.5.8,
# but very likely works for all later versions as well.
#
# The CREATE TABLE statements do not fully comply with SQL-92.
#
# MySQL extensions used here include :
# MEDIUMINT, UNSIGNED, ENUM, AUTO-INCREMENT, 
# DROP TABLE IF EXISTS, and the use of comments.
#
# SQL-92 validation tool:
# http://developer.mimer.se/validator/parser92/index.tml#parser
#
# The default table type is MyISAM, which is not transactional.
# The type is set InnoDB to allow the tables to participate
# in transactions.
#
# Run this script from the current directory in Windows like so:
# C:\myprojects\fish\WEB-INF\datastore\mysql> C:\mysql\bin\mysql --local-infile=1 -u myname -pmypass < CreateALL.SQL
#
# There are variations in MySQL syntax, according to the version of the database.
# Some users report that this is necessary:
#  - replace 'DROP DATABASE/CREATE DATABASE' with 'DROP SCHEMA/CREATE SCHEMA'

# Ensure the mysql client is using UTF-8
SET CHARACTER SET utf8;

DROP DATABASE IF EXISTS fish;
CREATE DATABASE fish CHARACTER SET = utf8;
USE fish;

# Create all Fish and Chips Tables.

# A Code Table
CREATE TABLE Disposition (
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Text VARCHAR(50)UNIQUE NOT NULL,
  ShortText VARCHAR(2)UNIQUE NOT NULL, /* Short forms for codes are especially useful for reports. */
  PRIMARY KEY (Id)
) ENGINE=InnoDB;

CREATE TABLE Resto (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Name VARCHAR(50) UNIQUE NOT NULL,
  Location VARCHAR(50),
  Price DECIMAL(5,2), 
  Comment VARCHAR(50),
  FishRating TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* 0..10 */
  ChipsRating TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* 0..10 */
  PriceRating TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* 0..10 */
  LocationRating TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* 0..10 */
  ServiceRating TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* 0..10 */
  BeerRating TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* 0..10 */
  PRIMARY KEY (Id)
) ENGINE=InnoDB;

CREATE TABLE Visit (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  RestoFK  MEDIUMINT UNSIGNED NOT NULL,
  LunchDate DATETIME NOT NULL,
  Message VARCHAR(50),
  PRIMARY KEY (Id),
  INDEX (RestoFK),
  FOREIGN KEY (RestoFK) REFERENCES Resto(Id)
) ENGINE=InnoDB;

CREATE TABLE Member (
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Name VARCHAR(50)UNIQUE NOT NULL,
  IsActive TINYINT UNSIGNED NOT NULL, /* Pseudo-boolean 1-true, 0-false */
  DispositionFK  MEDIUMINT UNSIGNED NOT NULL,
  PRIMARY KEY (Id),
  INDEX(DispositionFK),
  FOREIGN KEY (DispositionFK) REFERENCES Disposition(Id)
) ENGINE=InnoDB;

CREATE TABLE Discussion (
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Name VARCHAR(50) Default "???" NOT NULL, 
  Body TEXT NOT NULL,
  CreationDate DATETIME NOT NULL,
  PRIMARY KEY (Id)
) ENGINE=InnoDB;

CREATE TABLE Rsvp (  
  MemberFK MEDIUMINT UNSIGNED NOT NULL,
  VisitFK MEDIUMINT UNSIGNED NOT NULL,
  IsAttending TINYINT UNSIGNED NOT NULL, /* Pseudo-boolean 1-true, 0-false. Absence of an entry indicates no response at all. */
  PRIMARY KEY (MemberFK, VisitFK),
  INDEX (MemberFK),
  INDEX (VisitFK),
  FOREIGN KEY (MemberFK) REFERENCES Member(Id),
  FOREIGN KEY (VisitFK) REFERENCES Visit(Id)
) ENGINE=InnoDB;

CREATE TABLE Image (
 Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
 Description VARCHAR(50) DEFAULT NULL,
 Name VARCHAR(50) DEFAULT NULL,
 MimeType VARCHAR(50) DEFAULT NULL,
 Length INT UNSIGNED DEFAULT NULL,
 Img BLOB,
 PRIMARY KEY (Id)
) ENGINE=InnoDB;


# Populate Fish and Chips tables with some base data.
INSERT INTO Disposition(Id, Text, ShortText) VALUES (1, 'Sunny', 'SU');
INSERT INTO Disposition(Id, Text, ShortText) VALUES (2, 'Grumpy', 'GR');
INSERT INTO Disposition(Id, Text, ShortText) VALUES (3, 'Class Clown', 'CC');
INSERT INTO Disposition(Id, Text, ShortText) VALUES (4, 'Fluffy/Bouncy', 'FB');
INSERT INTO Disposition(Id, Text, ShortText) VALUES (5, 'Unflinchingly Neutral', 'UN');
INSERT INTO Disposition(Id, Text, ShortText) VALUES (6, 'Unpredictable If Cornered', 'UC');
INSERT INTO Disposition(Id, Text, ShortText) VALUES (7, 'Annoyingly Positive', 'AP');
INSERT INTO Disposition(Id, Text, ShortText) VALUES (8, 'Positively Annoying', 'PA');
INSERT INTO Member (Id, Name, IsActive, DispositionFK) VALUES(1,'Roger MacIntosh', 1, 1);
INSERT INTO Member (Id, Name, IsActive, DispositionFK) VALUES(2,'Husein Alisic', 1, 2);
INSERT INTO Member (Id, Name, IsActive, DispositionFK) VALUES(3,'Patrick Charles', 1, 3);
INSERT INTO Discussion (Id, Name, Body, CreationDate) VALUES(1,'System', 'This is a test of the Fish And Chips Messaging system. Do not be alarmed. It is not necessary to leave the building at this time.', '2006-11-16');
INSERT INTO Resto (Id, Name, Location, Price, Comment) VALUES(1,'Cedars', 'Across from ATC', 7.85, 'Test comment.');
INSERT INTO Visit (Id, RestoFK, LunchDate, Message) VALUES(1, 1, '2006-11-16', 'Test message.');



# ACCESS CONTROL
DROP DATABASE IF EXISTS fish_access;
CREATE DATABASE fish_access CHARACTER SET = utf8;
USE fish_access;

# Create Tables.

# MySQL has a system table called 'user', so that name cannot be used here.
CREATE TABLE Users (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Name VARCHAR(50) BINARY UNIQUE NOT NULL, /* Binary needed to ensure case sensitive. */
  Password VARCHAR(50) BINARY NOT NULL, /* Binary needed to ensure case sensitive. Must hash the password. */
  Language VARCHAR(25) NOT NULL DEFAULT 'en',
  FormSourceId VARCHAR(100) BINARY,   /* Defends against CSRF attacks. */
  PRIMARY KEY (Id)
) ENGINE=InnoDB;

# In the style demanded by Tomcat 4 (not a normalized form).
# Tomcat has the weird constraint that the column names must match between UserRole.Name and Users.Name.
CREATE TABLE UserRole (  
  Name VARCHAR(50) BINARY NOT NULL, 
  Role VARCHAR(50) BINARY NOT NULL, 
  INDEX (Name),
  FOREIGN KEY (Name) REFERENCES Users(Name),
  PRIMARY KEY (Name, Role)
) ENGINE=InnoDB;

# Populate tables with some base data.
# *** WARNING *** : this data is for development only, and must be DELETED from production environments.
# The passwords are in hashed form, and have the cleartext value 'testtest'. 
INSERT INTO Users (Id, Name, Password) VALUES(1, 'testeA', '51abb9636078defbf888d8457a7c76f85c8f114c');
INSERT INTO Users (Id, Name, Password) VALUES(2, 'testeB', '51abb9636078defbf888d8457a7c76f85c8f114c');
INSERT INTO Users (Id, Name, Password) VALUES(3, 'testeC', '51abb9636078defbf888d8457a7c76f85c8f114c');
INSERT INTO Users (Id, Name, Password) VALUES(4, 'testeD', '51abb9636078defbf888d8457a7c76f85c8f114c');

INSERT INTO UserRole (Name, Role) VALUES('testeA', 'user-general');
INSERT INTO UserRole (Name, Role) VALUES('testeB', 'translator');
INSERT INTO UserRole (Name, Role) VALUES('testeC', 'webmaster');
INSERT INTO UserRole (Name, Role) VALUES('testeD', 'user-general');
INSERT INTO UserRole (Name, Role) VALUES('testeD', 'translator');
INSERT INTO UserRole (Name, Role) VALUES('testeD', 'webmaster');
INSERT INTO UserRole (Name, Role) VALUES('testeD', 'user-president');
INSERT INTO UserRole (Name, Role) VALUES('testeD', 'access-control');


# TRANSLATION 

DROP DATABASE IF EXISTS fish_translation;
CREATE DATABASE fish_translation CHARACTER SET = utf8;
USE fish_translation;

# Create Tables.

# Tradeoff for BaseText.Text :
# The preference is for Base.Text to be both UNIQUE and case-sensitive. 
# For the target MySQL database, that is possible for 
#    Text VARCHAR(255) BINARY UNIQUE NOT NULL,
# but it is not possible for larger column types such as TEXT. 
# TEXT fields can have neither UNIQUE or BINARY attached to them.
# This likely a problem in other databases as well.
# In this implementation, larger items are cut off at 255 chars.
#
# For handling large text, the <w:txt> tag has a 'backup' mechanism, which 
# can be used in case of need. Since that mechanism does not use the usual 
# Translator interface, it is outside of the 'normal' mechanism, if possible.

CREATE TABLE BaseText (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Text VARCHAR(255) BINARY UNIQUE NOT NULL, /* Binary needed to ensure case sensitive */
  IsCoderKey TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* Pseudo-boolean 1-true, 0-false */
  PRIMARY KEY (Id)
) ENGINE=InnoDB;

CREATE TABLE Locale (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  ShortForm VARCHAR(20) UNIQUE NOT NULL,
  Description VARCHAR(50) NOT NULL, 
  PRIMARY KEY (Id)
) ENGINE=InnoDB;

CREATE TABLE Translation (
  BaseTextFK MEDIUMINT UNSIGNED NOT NULL,
  LocaleFK MEDIUMINT UNSIGNED NOT NULL,
  Text VARCHAR(255) BINARY NOT NULL,
  PRIMARY KEY (BaseTextFK, LocaleFK),
  INDEX (BaseTextFK),
  INDEX (LocaleFK),
  FOREIGN KEY (BaseTextFK) REFERENCES BaseText(Id),
  FOREIGN KEY (LocaleFK) REFERENCES Locale(Id)
) ENGINE=InnoDB;


CREATE TABLE UnknownBaseText (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Text VARCHAR(255) BINARY UNIQUE NOT NULL, /* Binary needed to ensure case sensitive */
  PRIMARY KEY (Id)
) ENGINE=InnoDB;


# Populate Fish and Chips tables with some base data.
INSERT INTO Locale (Id, ShortForm, Description) VALUES(1, 'en', 'English');
INSERT INTO Locale (Id, ShortForm, Description) VALUES(2, 'fr', 'French');

INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(1, 'Visits', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(2, 'Lunch Date', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(3, 'add.edit.button', 1);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(4, 'edit', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(5, 'delete', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(6, '#', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(7, '(MMDDYYYY)', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(8, 'Message', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(9, 'Restaurant', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(10, 'Date', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(11, 'Line Number', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(12, 'Visit no longer exists. Likely deleted by another user.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(13, 'No update occurred. Visit likely deleted by another user.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(14, 'Visit deleted successfully.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(15, 'Cannot delete. Visit used elsewhere.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(16, 'Visit to _1_ added successfully.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(17, 'Visit to _1_ changed successfully.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(18, 'Home', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(19, 'Rsvp', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(20, 'Discussion', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(21, 'Ratings', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(22, 'Restaurants', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(23, 'Members', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(24, '"The Total Fish & Chips Dining Experience"', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(25, 'logo.gif', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(26, 'Copyright', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(27, 'Fish And Chips', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(28, 'Lunch Date is required, year in range 2006..2100.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(29, 'What''s Next', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(30, 'format.next.lunch.date', 1);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(31, 'There is no scheduled future Visit.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(32, 'Num "Yes" Responses', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(33, 'Active Members', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(34, 'Yes', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(35, 'No', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(36, 'Name', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(37, 'Add Rsvp', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(38, 'Change Rsvp', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(39, 'Y', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(40, 'N', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(41, 'is coming to this lunch?', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(42, 'Visit Id is required, 1..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(43, 'Member Id is required, 1..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(44, 'Member Name is optional, 2..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(45, 'Comment', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(46, 'Post Comment', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(47, 'Comments', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(48, 'Comment body must have content.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(49, 'Restaurant Ratings', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(50, 'Overall Rating for restaurant', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(51, 'Fish', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(52, 'Chips', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(53, 'Price', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(54, 'Location', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(55, 'Service', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(56, 'Beer', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(57, 'Update', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(58, 'Weight: 1', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(59, 'Weight: 2', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(60, 'Weight: 3', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(61, 'Overall', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(62, 'Cannot Fetch Listing of Comments', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(63, 'Id is optional, 1..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(64, 'Restaurant Name is required, 2..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(65, 'Message is optional, 2..50 characters.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(66, '_1_ is not in a number format : ''_2_''', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(67, '_1_ is not in the expected format/range : ''_2_''', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(68, '_1_ is not an integer : ''_2_''', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(69, '_1_ is not in the expected date format(MMDDYYY) : ''_2_''', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(70, 'Id is required, 1..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(71, 'Name is optional, 2..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(72, 'Fish Rating is required, 0..10.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(73, 'Chips Rating is required, 0..10.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(74, 'Price Rating is required, 0..10.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(75, 'Location Rating is required, 0..10.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(76, 'Service Rating is required, 0..10.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(77, 'Beer Rating is required, 0..10.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(78, 'Overall Rating is optional, 0.00..10.00.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(79, 'Cannot fetch the selected item. Likely deleted by another user.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(80, 'Update failed. Has item been deleted by another user?', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(81, 'Location is optional, 2..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(82, 'Price is optional, 0.00 to 100.00.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(83, 'Comment is optional, 2..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(84, '_1_ added successfully.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(85, 'Restaurant Name already taken. Please use another name.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(86, 'Item no longer exists. Likely deleted by another user.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(87, '_1_ changed successfully.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(88, 'No update occurred. Item likely deleted by another user.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(89, 'Item deleted successfully.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(90, 'Cannot delete. Item likely used elsewhere.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(91, 'Name is required, 2..50 chars.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(92, 'Is Active is required. Inactive Members will not appear in the RSVP list.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(93, 'Member Name already taken. Please use another name.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(94, 'Is Active?', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(95, 'Refresh this page to reload translations.', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(96, 'Number of translations', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(97, 'Refresh', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(98, 'List', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(99, 'Locales', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(100, 'Base Text', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(101, 'Translation', 0);
INSERT INTO BaseText (Id, Text, IsCoderKey) VALUES(102, 'Refresh Translations', 0);


# This set of translations is only about 80% complete. 
# This is by design, since it let's you exercise some features of the application dealing with 
# translation.

INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(1, 2, 'Déjeuners');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(2, 2, 'Date Du Déjeuner');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(3, 1, 'Add/Edit');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(3, 2, 'Ajouter/Changer');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(4, 2, 'changer');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(5, 2, 'supprimer');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(6, 2, 'No.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(7, 2, '(MMJJAAAA)');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(8, 2, 'Commentaire');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(9, 2, 'Resto');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(10, 2, 'Date');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(11, 2, 'Numéro de Ligne');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(12, 2, 'Le déjeuner n''existe plus. Le déjeuner était probablement supprimé par quelqu''un d''autre.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(13, 2, 'Pas de mise à jour. Le déjeuner était probablement supprimé par quelqu''un d''autre.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(14, 2, 'Le déjeuner est supprimé.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(15, 2, 'Le déjeuner est en usage. Il n''est pas possible de le supprimer.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(16, 2, 'Le déjeuner chez _1_ est ajouté.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(17, 2, 'Le déjeuner chez _1_ est changé.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(18, 2, 'Accueil');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(19, 2, 'Rsvp');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(20, 2, 'Babillard');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(21, 2, 'Echelles');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(22, 2, 'Restos');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(23, 2, 'Membres');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(24, 2, '"L''Expérience Poisson Et Frites Complète"');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(25, 2, 'logo_fr.gif');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(26, 2, 'Tous droits réservés');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(27, 2, 'Poissons Et Frites');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(28, 2, 'La date du déjeuner est obligatoire, année entre 2006 et 2100.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(29, 2, 'Le Prochain Déjeuner');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(30, 1, 'E, MMM dd');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(30, 2, 'EEEE, dd MMM');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(31, 2, 'Il n''y a pas de futur déjeuner.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(32, 2, 'Nombre de réponses "Oui"');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(33, 2, 'Membres Actifs');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(34, 2, 'Oui');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(35, 2, 'Non');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(36, 2, 'Nom');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(37, 2, 'Ajouter RSVP');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(38, 2, 'Changer RSVP');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(39, 2, 'O');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(40, 2, 'N');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(41, 2, 'vient à ce déjeuner?');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(42, 2, 'Id de Déjeuner est obligatoire, 1..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(43, 2, 'Id de Membre est obligatoire, 1..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(44, 2, 'Nom du Membre est obligatoire, 2..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(45, 2, 'Commentaire');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(46, 2, 'Ajouter Un Commentaire');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(47, 2, 'Commentaires');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(48, 2, 'Le commentaire doit avoir du contenu.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(49, 2, 'Echelle des Restos');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(50, 2, 'Résumé des Echelles');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(51, 2, 'Poisson');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(52, 2, 'Frites');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(53, 2, 'Prix');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(54, 2, 'Localisation');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(55, 2, 'Service');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(56, 2, 'Bière');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(57, 2, 'Changer');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(58, 2, 'Coefficient: 1');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(59, 2, 'Coefficient: 2');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(60, 2, 'Coefficient: 3');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(61, 2, 'Résumé');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(62, 2, 'Le système ne peut pas trouver les commentaires.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(63, 2, 'Id est optionnel, 1..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(64, 2, 'Le nom du resto est obligatoire, 2..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(65, 2, 'Le commentaire est optionnel, 2..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(66, 2, '_1_ n''as pas le format d''un nombre : ''_2_''');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(67, 2, '_1_ n''a pas le format ou plage prévue : ''_2_''');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(68, 2, '_1_ n''a pas le format d''un nombre intégrale : ''_2_''');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(69, 2, '_1_ n''a pas le format prévu d''une date(MMJJAAAA) : ''_2_''');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(70, 2, 'Id est obligatoire, 1..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(71, 2, 'Le nom est facultatif, 2..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(72, 2, 'La cote du poisson est obligatoire, 0..10.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(73, 2, 'La cote des frites est obligatoire, 0..10.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(74, 2, 'La cote du prix est obligatoire, 0..10.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(75, 2, 'La cote de la localisation est obligatoire, 0..10.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(76, 2, 'La cote de la service est obligatoire, 0..10.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(77, 2, 'La cote de la bière est obligatoire, 0..10.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(78, 2, 'Le résumé est facultatif, 0.00..10.00.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(79, 2, 'L''article choisi n''est pas trouvé. Supprimé par un quelqu''un d''autre?');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(80, 2, 'La mise à jour n''a pas réussi. L''article était supprimé par un quelqu''un d''autre?');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(81, 2, 'La localisation est facultatif, 2..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(82, 2, 'Le prix est facultatif, 0.00 to 100.00.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(83, 2, 'La commentaire est facultatif, 2..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(84, 2, '_1_ ajouté avec succès.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(85, 2, 'Le nom du resto est doublé. Veuillez employer un autre nom.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(86, 2, 'L''article n''existe plus. Supprimé par quelqu''un d''autre?');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(87, 2, '_1_ changé avec succès.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(88, 2, 'Pas de mise à jour. L''article était supprimé par quelqu''un d''autre?');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(89, 2, 'L''article supprimé avec succès.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(90, 2, 'Pas possible de supprimer. Déjà supprimé par quelqu''un d''autre?');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(91, 2, 'Le nom est obligatoire, 2..50 caractères.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(92, 2, 'Actif(ve) est obligatoire. Les membres inactif n''apparaissent pas sur la liste RSVP.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(93, 2, 'Ce nom est doublé. Veuillez choisir un autre nom.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(94, 2, 'Actif(ve)');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(95, 2, 'Recharger cette page pour rafraîchir les traductions.');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(96, 2, 'Nombre de traductions');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(97, 2, 'Rafraîchir');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(98, 2, 'Liste');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(99, 2, 'Locales');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(100, 2, 'Text de Base');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(101, 2, 'Traduction');
INSERT INTO Translation (BaseTextFK, LocaleFK, Text) VALUES(102, 2, 'Rafraîchir Les Traductions');
