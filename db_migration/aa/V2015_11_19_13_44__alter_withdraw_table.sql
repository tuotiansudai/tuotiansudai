ALTER TABLE `aa`.`withdraw` CHANGE COLUMN `verify_time` `apply_notify_time` DATETIME;
ALTER TABLE `aa`.`withdraw` CHANGE COLUMN `verify_message` `apply_notify_message` VARCHAR(256);
ALTER TABLE `aa`.`withdraw` CHANGE COLUMN `recheck_time` `notify_time` DATETIME;
ALTER TABLE `aa`.`withdraw` CHANGE COLUMN `recheck_message` `notify_message` VARCHAR(256);
ALTER TABLE `aa`.`withdraw` ADD COLUMN `bank_card_id` BIGINT UNSIGNED
AFTER id;

ALTER TABLE `aa`.`withdraw` ADD CONSTRAINT FK_WITHDRAW_BANK_CARD_ID_REF_BANK_CARD_ID FOREIGN KEY (`bank_card_id`) REFERENCES `aa`.`bank_card` (`id`);

BEGIN;

UPDATE `aa`.`withdraw`
SET `status` = 'WAIT_PAY'
WHERE `status` = 'WAIT_VERIFY';

UPDATE `aa`.`withdraw`
SET `status` = 'APPLY_SUCCESS'
WHERE `status` = 'RECHECK';

UPDATE `aa`.`withdraw`
SET `status` = 'APPLY_FAIL'
WHERE `status` = 'VERIFY_FAIL';

UPDATE `aa`.`withdraw`
SET `status` = 'FAIL'
WHERE `status` = 'RECHECK_FAIL';


COMMIT;