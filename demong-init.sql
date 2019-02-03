DROP schema `demong`;

CREATE SCHEMA `demong`;

USE `demong`;

CREATE TABLE `person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fname` varchar(32) NOT NULL,
  `lname` varchar(32) NOT NULL,
  `mname` varchar(32) DEFAULT NULL,
  `alias` varchar(32) DEFAULT NULL,
  `title` varchar(20) DEFAULT NULL,
  UNIQUE(id),
  PRIMARY KEY(id),
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE person AUTO_INCREMENT=1000;