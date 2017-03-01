BEGIN;

ALTER TABLE `aa`.`coupon` 
DROP COLUMN `sms_alert`;

COMMIT;