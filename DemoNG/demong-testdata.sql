USE `demong`;

INSERT INTO person (fname, lname, mname, alias, title) VALUES ("Mickey", "Mouse", "Disney", "MM", "Mr");
INSERT INTO person (fname, lname, mname, alias, title) VALUES ("Bugs", "Bunny", "Warner", "Bugsy", "Mr");
INSERT INTO person (fname, lname, mname, alias, title) VALUES ("Yosemite", "Sam", "Warner", "", "Mr");
INSERT INTO person (fname, lname, mname, alias, title) VALUES ("Road","Runner","Warner","Bird Brain","");
INSERT INTO person (fname, lname, mname, alias, title) VALUES ("Wile E","Coyote","Warner","","Mr");
INSERT INTO person (fname, lname, mname, alias, title) VALUES ("Fred","Flinstone","Hanna Barbera","Rocky","Mr");
INSERT INTO person (fname, lname, mname, alias, title) VALUES ("Woody","Woodpecker","Walter Lantz","","");

SELECT * from person;