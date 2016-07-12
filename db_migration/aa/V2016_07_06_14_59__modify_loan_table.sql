BEGIN;
ALTER TABLE `aa`.`loan`
ADD COLUMN `pledge_type` VARCHAR(30) NOT NULL
AFTER `description_html`;
COMMIT;
