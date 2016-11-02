BEGIN;
ALTER TABLE `aa`.`message`
  ADD COLUMN `template_txt` TEXT NULL DEFAULT NULL
  AFTER `template`,
  ADD COLUMN `manual_message_type` VARCHAR(30) NULL DEFAULT NULL
  AFTER `channels`,
  ADD COLUMN `web_url` VARCHAR(200) NULL DEFAULT NULL
  AFTER `manual_message_type`,
  ADD COLUMN `app_url` VARCHAR(30) NULL DEFAULT NULL
  AFTER `web_url`;
COMMIT;