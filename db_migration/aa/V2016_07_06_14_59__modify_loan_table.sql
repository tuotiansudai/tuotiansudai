BEGIN;
ALTER TABLE `aa`.`loan`
DROP COLUMN `loaner_identity_number`,
DROP COLUMN `loaner_user_name`,
DROP COLUMN `loaner_login_name`,
ADD COLUMN `pledge_type` VARCHAR(30) NOT NULL
AFTER `description_html`;
COMMIT;
