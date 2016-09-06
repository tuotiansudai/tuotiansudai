ALTER TABLE `question` ADD COLUMN `mobile` VARCHAR(11)
AFTER `login_name`;

ALTER TABLE `question` ADD COLUMN `fake_mobile` VARCHAR(11)
AFTER `mobile`;

ALTER TABLE `answer` ADD COLUMN `mobile` VARCHAR(11)
AFTER `login_name`;

ALTER TABLE `answer` ADD COLUMN `fake_mobile` VARCHAR(11)
AFTER `mobile`;