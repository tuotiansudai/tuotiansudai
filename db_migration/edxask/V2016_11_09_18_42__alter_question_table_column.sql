BEGIN;

ALTER TABLE `edxask`.`question`
  CHANGE COLUMN `question` `question` VARCHAR(100) NOT NULL;

COMMIT;