/*
 * This file is part of the setup of the DemoNG database that I 
 * use for demonstrating software concepts/technologies.
 *
 * DemoNG is basically the backend of an application used to manage
 * a set of soccer clubs, their teams, fixtures, members etc.
 * Its primary intention is to help me learn, giving me a platform to
 * develop new skills/ideas. Learn by doing!
 *
 * Design/Implementation principles:
 * + While performance is important, its not main goal for this database.
 * + Rules may be important within a club, but rules change. This database
 *   is not intended to implement club/ rules (eg that a player could be 
 *   playing for multiple teams or is only eligible to play u18 if their 
 *   birthday is before a certain cut-off date.
 * + While I am using MySQL to develop the database, I want to avoid using
 *   MySQLisms; that is, constructions/syntax that only work in MySQL or are
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
DROP schema `demong`;

CREATE SCHEMA `demong`;

USE `demong`;

/*
 * The person table is used to store informatoin about ANYONE that the 
 * is involved in the game.
 * Its not confined to club members or officials.
 * 
 * How to handle things like membership?
 * Perhaps that's outside the scope of this.
 * Members can come and go, move clubs etc, but people are the same.
 * May be able to to drop the "is_*" attributes.
 * But that will require tables to be setup for each of the types
 * of person we are interested in tracking.
 */
CREATE TABLE `person` (
  `person_id` INTEGER NOT NULL AUTO_INCREMENT,
  UNIQUE(person_id),
  PRIMARY KEY(person_id),
  `fname` VARCHAR(32) NOT NULL,
  `lname` VARCHAR(32) NOT NULL,
  `mname` VARCHAR(32) DEFAULT NULL,
  `alias` VARCHAR(32) DEFAULT NULL,
  `title` VARCHAR(20) DEFAULT NULL,
  `dob`  DATE DEFAULT NULL,
  `sex` ENUM('Male', 'Female', 'N/A') DEFAULT NULL,
  `image` blob DEFAULT NULL,

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

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE person AUTO_INCREMENT=1000;

/*
 * Every team must be associated with a club.
 * Clubs have addresses, logos, committees etc.
 */
CREATE TABLE `club` (
  `club_id` INTEGER NOT NULL AUTO_INCREMENT,
  UNIQUE(club_id),
  PRIMARY KEY(club_id),
  `name` VARCHAR(32) NOT NULL,
  `logo` blob DEFAULT NULL,

  `addressline1` varchar(32) DEFAULT NULL, -- House/Building name
  `addressline2` varchar(32) DEFAULT NULL, -- Street and number
  `addressline3` varchar(32) DEFAULT NULL, -- District
  `addressline4` varchar(32) DEFAULT NULL, -- City
  `addressline5` varchar(32) DEFAULT NULL, -- Postcode
  `addressline6` varchar(32) DEFAULT NULL, -- Country

  `firstreg` datetime DEFAULT NULL,
  `secretary_id` INTEGER NOT NULL REFERENCES person(person_id), 
  `treasurer_id` INTEGER NOT NULL REFERENCES person(person_id), 
  `president_id` INTEGER NOT NULL REFERENCES person(person_id) 

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE club AUTO_INCREMENT=1000;

/*
 * Every team is associated with a club, the team will have a name and it
 * it will have a set of players.
 * The players and the teams they can play in are stored in another table.
 */
CREATE TABLE `team` (
  `team_id` INTEGER NOT NULL AUTO_INCREMENT,
  UNIQUE(club_id),
  PRIMARY KEY(club_id),
  `name` VARCHAR(32) NOT NULL,
  FOREIGN KEY (manager_id) REFERENCES person (person_id),
  
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE club AUTO_INCREMENT=1000;

/*
 * This is where we store details about what teams people are in.
 * A person could be in multiple teams.
 * A person cannot appear twice in the same team.
 * A person should only appear in a team that is part of a club
 * that the person is a member of.
 * I don't see scope for "updating" a record like this, but will leave
 * it in anyhow.
 */
CREATE TABLE `team_members` (
  FOREIGN KEY (team_id) REFERENCES person (person_id),
  FOREIGN KEY (person_id) REFERENCES person (person_id),

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
 * This "competition" could be renamed to "tournament".
 * Egs of competition could be "league table", "u16 girls", "the cup", etc.
 */
CREATE TABLE `competition` (
  `competition_id` INTEGER NOT NULL AUTO_INCREMENT,
  UNIQUE(competition_id),
  PRIMARY KEY(competition_id),
  `name` VARCHAR(32) NOT NULL,
  `comp_logo` blob DEFAULT NULL,
  `comp_description` VARCHAR(256) DEFAULT NULL,  
  `comp_owner_id` INTEGER NOT NULL REFERENCES person(person_id),

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE competition AUTO_INCREMENT=1000;

/*
 * This is where we store details about what competitions teams are in.
 * A team could be in multiple competitions.
 * A team cannot appear twice in the same competition.
 * I don't see scope for "updating" a record like this, but will leave
 * it in anyhow.
 */
CREATE TABLE `competition_members` (
  FOREIGN KEY (team_id) REFERENCES team (team_id),
  FOREIGN KEY (competition_id) REFERENCES competition (competition_id),

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*
 * Could also have the concept of venues and pitches.
 * A single venure can have 1 or more pitches.
 * For a venue, probably also need address attributes.
 */
CREATE TABLE `pitch` (
  `pitch_id` INTEGER NOT NULL AUTO_INCREMENT,
  UNIQUE(pitch_id),
  PRIMARY KEY(pitch_id),
  `pitch_name` VARCHAR(32) NOT NULL,
  `pitch_description` VARCHAR(256) DEFAULT NULL,  
  `pitch_owner_id` INTEGER NOT NULL REFERENCES person(person_id),

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE pitch AUTO_INCREMENT=1000;

/*
 * I'm struggling with this a bit.
 * May need to think about squad....within than there's a team.
 * But not everyone on a team might play at a fixture - eg substitutions.
 * Eligibility to be on a team, but perhaps not play on the day (eg suspension).
 */
CREATE TABLE `team` (
  `team_id` INTEGER NOT NULL AUTO_INCREMENT,
  UNIQUE(team_id),
  PRIMARY KEY(team_id),
  `team_name` VARCHAR(32) NOT NULL,
  `team_description` VARCHAR(256) DEFAULT NULL,  
  `team_manager_id` INTEGER NOT NULL REFERENCES person(person_id),
  `team_club_id` INTEGER NOT NULL REFERENCES club(club_id),

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE team AUTO_INCREMENT=1000;

/*
 * A "fixture" could also be renamed a "game" or "event".
 * The intent here is to pull togther all the information about the fixture/event.
 * This table is just a start.
 * Its silent for example on start/end times, yellow and reds cards, etc.
 * It may make sense to have a separate "reports" table...that could contain
 * all sorts of reports from referees, managers, observers, anyone.
 *
 * A fixture has to be seen, in at least 2 ways:
 * + looking forward - telling the clubs/teams where/when etc. In this case
 *   on the day of the fixture, it should be possible to use realtime information
 *   about a team to pre-populate fixture record.
 * + looking back - a system of RECORD, where the details are captured.
 *   For example, what team played does not tell us WHO actually played on the day.
 *   That's because people move between teams over time, they could even move clubs.
 * So capturing this duality of purpose is the challenge for this table.
 * So for now, we have fixture -> team -> team_members - but what if the team
 * members change AFTER the fixture has been played....I think we might end up having
 * to control that somehow.
 * It could be that the fixture is setup using the current data, but once the fixture
 * is underway, that information gets moved to a match report....where it can be 
 * added to, but not changed (or changed without records being kept etc).
 *
 */
CREATE TABLE `fixture` (
  `fixture_id` INTEGER NOT NULL AUTO_INCREMENT,
  UNIQUE(fixture_id),
  PRIMARY KEY(fixture_id),
  `fixture_name` VARCHAR(32) NOT NULL,
  `fixture_description` VARCHAR(256) DEFAULT NULL,

  `fixture_date` datetime DEFAULT NULL,

  `fixture_home_team_id` INTEGER NOT NULL REFERENCES person(team_id),
  `fixture_away_team_id` INTEGER NOT NULL REFERENCES person(team_id),

  `fixture_pitch_id` INTEGER NOT NULL REFERENCES person(pitch_id),

  `fixture_referee_id` INTEGER NOT NULL REFERENCES person(person_id),
  `fixture_linesman1_id` INTEGER NOT NULL REFERENCES person(person_id),
  `fixture_linesman2_id` INTEGER NOT NULL REFERENCES person(person_id),

  `fixture_referee_report` TEXT DEFAULT NULL,
  `fixture_linesman1_report` TEXT DEFAULT NULL,
  `fixture_linesman2_report` TEXT DEFAULT NULL,

  `fixture_home_team_score` INTEGER DEAFULT NULL,
  `fixture_away_team_score` INTEGER DEFAULT NULL,

  FOREIGN KEY (fixture_home_team_id) REFERENCES team (team_id),
  FOREIGN KEY (fixture_away_team_id) REFERENCES fixture (fixture_id),

  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE fixture_id AUTO_INCREMENT=1000;
