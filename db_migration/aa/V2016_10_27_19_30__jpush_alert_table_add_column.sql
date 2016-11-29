BEGIN;
ALTER TABLE `aa`.`jpush_alert`
  ADD COLUMN `message_id` BIGINT(20) UNSIGNED NULL DEFAULT NULL
  AFTER `android_open_num`;
COMMIT;