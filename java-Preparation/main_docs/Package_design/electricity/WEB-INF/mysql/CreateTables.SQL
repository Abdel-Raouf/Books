# This is a MySQL script.
# It creates a database named 'electricity', and adds tables to it.
# It has been verified against MySQL version 5.5.8.
#
# If you prefer to use a different database other than MySQL, then porting this script should be simple.
#
# Example of running this script using the mysql client :
# [this directory]>C:\mysql\bin\mysql --local-infile=1 -u myname -pmypass < CreateTables.SQL
#
create database electricity CHARACTER SET = utf8;
use electricity;
# This is a code table.
CREATE TABLE Facility (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Name VARCHAR(100),
  PRIMARY KEY (Id)
) Engine=InnoDB;
# This is not a code table; it contains data entered by the user.
CREATE TABLE Spending (  
  Id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  DatePaid DATE NOT NULL,
  Amount DECIMAL(7,2) NOT NULL, 
  KWH DECIMAL(5,1), 
  IsEstimated TINYINT UNSIGNED DEFAULT 0 NOT NULL, /* Pseudo-boolean 1-true, 0-false. */
  FacilityFK  MEDIUMINT UNSIGNED, /* Foreign key points to code table entry.*/
  Comment VARCHAR(100),
  PRIMARY KEY (Id),
  INDEX(FacilityFK),
  FOREIGN KEY (FacilityFK) REFERENCES Facility(Id)
) Engine=InnoDB;
INSERT INTO Facility(Id, Name) VALUES (1, 'Residence');
INSERT INTO Facility(Id, Name) VALUES (2, 'Commercial');
