begin;
ALTER TABLE `aa`.`bank_account`
  ADD COLUMN `authorization_amount`  BIGINT UNSIGNED NOT NULL DEFAULT 0 AFTER `auto_invest`;

ALTER TABLE `aa`.`bank_account`
  ADD COLUMN `authorization_end_time`  DATETIME AFTER `authorization_amount`;

COMMIT;
