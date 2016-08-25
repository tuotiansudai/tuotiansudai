ALTER TABLE `aa`.`activity` ADD `seq` BIGINT NOT NULL;
ALTER TABLE `aa`.`activity` ADD `long_term` TINYINT(1) NOT NULL;
ALTER TABLE `aa`.`activity` MODIFY `expired_time` DATETIME DEFAULT NULL;
