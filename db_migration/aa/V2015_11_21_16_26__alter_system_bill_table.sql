ALTER TABLE `aa`.`system_bill` CHANGE `type` `operation_type` VARCHAR(8) NOT NULL;

ALTER TABLE `aa`.`system_bill` CHANGE `detail` `detail` TEXT AFTER `business_type`;