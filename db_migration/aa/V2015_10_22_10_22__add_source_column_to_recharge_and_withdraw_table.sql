ALTER TABLE `aa`.`recharge` ADD `source` VARCHAR(10) NOT NULL DEFAULT 'WEB' AFTER `status`;
ALTER TABLE `aa`.`recharge` ADD `fast_pay` BOOLEAN NOT NULL DEFAULT FALSE AFTER `source`;
ALTER TABLE `aa`.`withdraw` ADD `source` VARCHAR(10) NOT NULL DEFAULT 'WEB' AFTER `status`;