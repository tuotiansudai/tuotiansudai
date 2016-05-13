ALTER TABLE `aa`.`feedback` ADD COLUMN `source` VARCHAR(20) NOT NULL AFTER `login_name`;
ALTER TABLE `aa`.`feedback` ADD COLUMN `type` VARCHAR(20) NOT NULL AFTER `source`;
ALTER TABLE `aa`.`feedback` ADD COLUMN `status` VARCHAR(10) NOT NULL AFTER `type`;
ALTER TABLE `aa`.`feedback` CHANGE COLUMN `login_name` `login_name` VARCHAR(25);
ALTER TABLE `aa`.`feedback` ADD COLUMN `contact` VARCHAR(50) AFTER `login_name`;
