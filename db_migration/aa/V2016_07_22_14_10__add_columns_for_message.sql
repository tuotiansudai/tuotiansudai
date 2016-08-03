ALTER TABLE `message` ADD COLUMN `app_title` VARCHAR(100)
AFTER `title`;

ALTER TABLE `user_message` ADD COLUMN `app_title` VARCHAR(100)
AFTER `title`;