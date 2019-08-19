/*
 * This file is part of the setup of the DemoNG database that is 
 * used for demonstrating software concepts/technologies.
 *
 * DemoNG database is the backend of an application used to manage
 * a set of soccer clubs, their teams, fixtures, members etc.
 * Its primary intention is to help me learn, giving me a platform to
 * develop new skills/ideas. Learn by doing!
 *
 * Design/Implementation principles:
 * + While performance is important, its not the main goal for this database.
 * + Rules may be important within a club, but rules change. This database
 *   is not intended to implement club/association/competition rules (eg that 
 *   a player could be playing for multiple teams or is only eligible to play 
 *   u18 if their birthday is before a certain cut-off date.)
 * + While I am using MySQL to develop the database, I want to avoid using
 *   MySQLisms; that is, constructions/syntax that only work in MySQL or
 *   have some specialized quirky behaviour.
 *   - No TINYINT
 *   - I have used datetime (not in other SQLs, but there are equivalents)
 *   - I have used BLOB! - Binary Large OBject to store images.
 *   - I have used ENUM! - That may need a revisit.
 * + I could enforce that there will be 11 players on each team and that 
 *   there are 15 players in a squad and etc etc....but its not a design 
 *   goal. The officials on the day should be looking after that.
 * + Feedback is welcome, but please be gentle and use simple language I
 *   can quickly understand :-).
 */

/*
 * MySQL does not differentiate between DATABASE and SCHEMA.
 * Ordinarily, as I understand it, a SCHEMA is a collection of tables
 * and a DATABASE is a collection of SCHEMAs.
 * Separating out tables into schemas allows things like security
 * policies to be put in place, but these are not a feature of MySQL.
 */
DROP DATABASE IF EXISTS `demong`;
/*
DROP schema `demong`;
*/

CREATE SCHEMA `demong`;

USE `demong`;

/*
 * The person table is used to store information about ANYONE that the 
 * is involved in the game.
 * Its not confined to players, club members or officials.
 */
CREATE TABLE `person` (
  `person_id` INTEGER NOT NULL,
  `firstname` VARCHAR(32) NOT NULL,
  `lastname` VARCHAR(32) NOT NULL,
  `middlename` VARCHAR(32) DEFAULT NULL,
  `alias` VARCHAR(32) DEFAULT NULL,
  `title` VARCHAR(20) DEFAULT NULL,
  `dob`  DATE DEFAULT NULL,
  `sex` ENUM('M', 'F', 'N/A') DEFAULT NULL,
  `image` blob DEFAULT NULL,
  `imagelocation` VARCHAR(256) DEFAULT NULL,
  `url` VARCHAR(256) DEFAULT NULL,

  /*
   * We could put addresses in a separate table and link
   * from here to the address table.
   * For now, this is how it is.
   */
  `addressline1` varchar(32) DEFAULT NULL, -- House/Building name
  `addressline2` varchar(32) DEFAULT NULL, -- Street and number
  `addressline3` varchar(32) DEFAULT NULL, -- District
  `addressline4` varchar(32) DEFAULT NULL, -- City
  `addressline5` varchar(32) DEFAULT NULL, -- Postcode
  `addressline6` varchar(32) DEFAULT NULL, -- Country

  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-person.csv' 
INTO TABLE person 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;

ALTER TABLE person 
CHANGE COLUMN person_id 
person_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY;


/*
 * Every team must be associated with a club.
 * Clubs have addresses, logos, committees etc.
 */
CREATE TABLE `club` (
  `club_id` INTEGER NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `logo` blob DEFAULT NULL,
  `logolocation` VARCHAR(256) DEFAULT NULL,

  `addressline1` varchar(32) DEFAULT NULL, -- House/Building name
  `addressline2` varchar(32) DEFAULT NULL, -- Street and number
  `addressline3` varchar(32) DEFAULT NULL, -- District
  `addressline4` varchar(32) DEFAULT NULL, -- City
  `addressline5` varchar(32) DEFAULT NULL, -- Postcode
  `addressline6` varchar(32) DEFAULT NULL, -- Country

  `from_date` datetime DEFAULT NULL,
  `to_date` datetime NOT NULL,

  `chairman_id` VARCHAR(32),
  `headcoach_id` INTEGER NOT NULL,
  FOREIGN KEY (`headcoach_id`)
  REFERENCES person (`person_id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  `secretary_id` INTEGER, 
  `treasurer_id` INTEGER, 
  `president_id` INTEGER, 

  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-club.csv' 
INTO TABLE club 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;

ALTER TABLE club 
CHANGE COLUMN club_id 
club_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY;

/*
 * I'm struggling with this a bit.
 * The "Team" - plays on the day.
 * But on different days, different people may play for the same team.
 * Teams use the club logo and address etc - but they could also have sponsors and sponsor logos etc.
 * May need to think about squad/panel....within the squad there's a team.
 * But not everyone on a team might play at a fixture - eg substitutions.
 * Eligibility to be on a team, but perhaps not play on the day (eg suspension).
 *
 * Every team is associated with a club, the team will have a name and it
 * It will have a set of players.
 * The players and the teams they can play in are stored in another table.
 */
CREATE TABLE `team` (
  `team_id` INTEGER NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(256) DEFAULT NULL,  
  `manager_id` INTEGER NOT NULL REFERENCES person(person_id),
  `club_id` INTEGER NOT NULL REFERENCES club(club_id),

  FOREIGN KEY (`manager_id`)
  REFERENCES person (`person_id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  FOREIGN KEY (`club_id`) 
  REFERENCES club (`club_id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET=utf8mb4;

LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-team.csv' 
INTO TABLE team 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;

ALTER TABLE team 
CHANGE COLUMN team_id 
team_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY;


/*
 * This is where we store details about what teams people are in.
 * A person could be in multiple teams.
 * A person cannot appear twice in the same team.
 * A person should only appear in a team that is part of a club
 * that the person is a member of.
 * I don't see scope for "updating" a record like this, but will leave
 * it in anyhow.
 */
CREATE TABLE `team_member` (
  team_id INTEGER NOT NULL,
  person_id INTEGER NOT NULL,

  FOREIGN KEY (team_id) 
  REFERENCES team (team_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  FOREIGN KEY (person_id) 
  REFERENCES person (person_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  `from_date` datetime DEFAULT '2019-01-01',
  `to_date` datetime,

  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-team-member.csv' 
INTO TABLE team_member 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;


ALTER TABLE team_member ADD PRIMARY KEY (team_id, person_id);

/*
 * This "competition" could be renamed to "tournament".
 * Egs of competition could be "league table", "u16 girls", "the cup", etc.
 */
CREATE TABLE `competition` (
  `competition_id` INTEGER NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(256) DEFAULT NULL,  
  `logo` blob DEFAULT NULL,
  `logolocation` VARCHAR(256) DEFAULT NULL,
  `contact_id` INTEGER NOT NULL,

  FOREIGN KEY (contact_id)
  REFERENCES person (person_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET=utf8mb4;


LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-competition.csv' 
INTO TABLE competition 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;

ALTER TABLE competition 
CHANGE COLUMN competition_id competition_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY;

/*
 * This is where we store details about what competitions teams are in.
 * A team could be in multiple competitions.
 * A team cannot appear twice in the same competition.
 * I don't see scope for "updating" a record like this, but will leave
 * it in anyhow.
 */
CREATE TABLE `competition_team` (
  `competition_id` INTEGER NOT NULL,
  `team_id` INTEGER NOT NULL,

  FOREIGN KEY (competition_id) 
  REFERENCES competition (competition_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  FOREIGN KEY (team_id)
  REFERENCES team (team_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*
LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-competition-team.csv' 
INTO TABLE competition_team 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;

LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-competition-team.csv' INTO TABLE competition_team FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
 */

ALTER TABLE competition_team ADD PRIMARY KEY (competition_id, team_id);

/*
 * Could also have the concept of venues and pitches.
 * A single venue can have 1 or more pitches.
 * For a venue, probably also need address attributes.
 *
 * I think I will end up making pitches owned by a club rather than a person.
 * Manbe its not so much an owner as a controller/manager.
 */
CREATE TABLE `pitch` (
  `pitch_id` INTEGER NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(256) DEFAULT NULL,  
  `coords` VARCHAR(32) DEFAULT NULL,  
  `contact_id` INTEGER NOT NULL REFERENCES person(person_id),

  `addressline1` varchar(32) DEFAULT NULL, -- House/Building name
  `addressline2` varchar(32) DEFAULT NULL, -- Street and number
  `addressline3` varchar(32) DEFAULT NULL, -- District
  `addressline4` varchar(32) DEFAULT NULL, -- City
  `addressline5` varchar(32) DEFAULT NULL, -- Postcode
  `addressline6` varchar(32) DEFAULT NULL, -- Country

  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET=utf8mb4;

/*
LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-pitch.csv' 
INTO TABLE pitch
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;

LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-pitch.csv' INTO TABLE pitch FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
 */

ALTER TABLE pitch
CHANGE COLUMN pitch_id 
pitch_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY;

/*
 * A "fixture" could also be renamed a "game" or "event".
 * The intent here is to pull togther all the information about the i
 * fixture/event.
 * This table is just a start.
 * Basically a fixture is a combination of:
 * + pitch_id
 * + home team/team_id
 * + away team/team_id
 * + referee/person_id
 * + fixture_datetime
 * Linesmen and competitions are not included.
 *
 * Its silent for example on start/end times, yellow and reds cards, etc.
 * It may make sense to have a separate "reports" table...that could contain
 * all sorts of reports from referees, managers, observers, anyone.
 *
 * A fixture has to be seen, in at least 2 ways:
 * + looking forward - telling the clubs/teams where/when etc. In this case
 *   on the day of the fixture, it should be possible to use realtime 
 *   information about a team to pre-populate fixture record.
 * + looking back - a system of RECORD, where the details are captured.
 *   For example, what team played does not tell us WHO actually played 
 *   on the day.
 *   That's because people move between teams over time, they could even 
 *   move clubs.
 * So capturing this duality of purpose is the challenge for this table.
 * So for now, we have fixture -> team -> team_members - but what if the team
 * members change AFTER the fixture has been played....I think we might end 
 * up having to control that somehow.
 * It could be that the fixture is setup using the current data, but once 
 * the fixture is underway, that information gets moved to a match report....
 * where it can be added to, but not changed (or changed without records 
 * being kept etc).
 *
 */
CREATE TABLE `fixture` (
  `fixture_id` INTEGER NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(256) DEFAULT NULL,

  `date` datetime DEFAULT NULL,

  `home_team_id` INTEGER NOT NULL REFERENCES person(team_id),
  `away_team_id` INTEGER NOT NULL REFERENCES person(team_id),

  `pitch_id` INTEGER NOT NULL REFERENCES person(pitch_id),

  `referee_id` INTEGER NOT NULL REFERENCES person(person_id),
  `linesman1_id` INTEGER NOT NULL REFERENCES person(person_id),
  `linesman2_id` INTEGER NOT NULL REFERENCES person(person_id),

  `referee_report` TEXT DEFAULT NULL,
  `linesman1_report` TEXT DEFAULT NULL,
  `linesman2_report` TEXT DEFAULT NULL,

  `home_team_score` INTEGER DEFAULT NULL,
  `away_team_score` INTEGER DEFAULT NULL,

  FOREIGN KEY (home_team_id)
  REFERENCES team (team_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  FOREIGN KEY (away_team_id)
  REFERENCES team (team_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,

  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET=utf8mb4;


/*LOAD DATA
LOCAL INFILE 'C:/GitRepo/DemoNG/demong-testdata-fixture.csv' 
INTO TABLE fixture
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n' 
set created_at = CURRENT_TIMESTAMP;

LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-fixture.csv' INTO TABLE fixture FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
 */

ALTER TABLE fixture
CHANGE COLUMN fixture_id 
fixture_id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY;


/*
DELETE from person;

I'm still getting issues with DoB in Person when I specify 1937 instead of 1 1 1937/..it warns that the DoB is truncated.
Also having issues with truncation for created at, deleted at & updated at.
Also, I get this a lot: Warning (Code 1062): Duplicate entry '1201' for key 'PRIMARY'
Solving these issues will I hope lead to better setup script and clear the way for more progress.


LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-person.csv' INTO TABLE person FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-club.csv' INTO TABLE club FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-team.csv' INTO TABLE team FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-team-member.csv' INTO TABLE team_member FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-competition.csv' INTO TABLE competition FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-competition-team.csv' INTO TABLE competition_team FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-pitch.csv' INTO TABLE pitch FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
LOAD DATA LOCAL INFILE 'C:\\GitRepo\\DemoNG\\demong-testdata-fixture.csv' INTO TABLE fixture FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n' SET created_at = CURRENT_TIMESTAMP;
 */
 
 
