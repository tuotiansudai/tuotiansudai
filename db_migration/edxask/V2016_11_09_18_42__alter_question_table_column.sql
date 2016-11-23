BEGIN;

ALTER TABLE `edxask`.`question`
  CHANGE COLUMN `question` `question` VARCHAR(200) NOT NULL;

COMMIT;