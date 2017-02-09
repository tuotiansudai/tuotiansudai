BEGIN;

ALTER TABLE `edxmessage`.`message`
CHANGE COLUMN `expired_time` `valid_end_time` DATETIME NOT NULL ,
ADD COLUMN `valid_start_time` DATETIME NOT NULL DEFAULT "0001-01-01 00:00:00" AFTER `activated_time`;

COMMIT;