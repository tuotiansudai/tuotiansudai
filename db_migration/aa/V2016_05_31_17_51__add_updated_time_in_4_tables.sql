ALTER TABLE `aa`.`invest_repay` ADD COLUMN `updated_time` datetime NOT NULL AFTER `created_time`;
ALTER TABLE `aa`.`invest` ADD COLUMN `updated_time` datetime NOT NULL AFTER `created_time`;
ALTER TABLE `aa`.`recharge` ADD COLUMN `updated_time` datetime NOT NULL AFTER `created_time`;
ALTER TABLE `aa`.`withdraw` ADD COLUMN `updated_time` datetime NOT NULL AFTER `created_time`;

BEGIN ;
update `aa`.`invest_repay` set updated_time = created_time;
update `aa`.`invest` set updated_time = created_time;
update `aa`.`recharge` set updated_time = created_time;
update `aa`.`withdraw` set updated_time = created_time;
COMMIT ;
