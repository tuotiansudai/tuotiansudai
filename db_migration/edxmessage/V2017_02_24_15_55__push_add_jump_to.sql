BEGIN;

ALTER TABLE `edxmessage`.`push`
ADD COLUMN `jump_to` VARCHAR(50) NOT NULL AFTER `content`;

UPDATE `edxmessage`.`push` SET `jump_to` = 'MESSAGE_CENTER_LIST';

COMMIT;