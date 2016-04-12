ALTER TABLE `aa`.`invest_referrer_reward` CHANGE COLUMN `time` `created_time` DATETIME NOT NULL;
ALTER TABLE `aa`.`invest_referrer_reward` CHANGE COLUMN `bonus` `amount` BIGINT UNSIGNED DEFAULT NULL;
ALTER TABLE `aa`.`invest_referrer_reward` CHANGE COLUMN `role_name` `referrer_role` VARCHAR(20);