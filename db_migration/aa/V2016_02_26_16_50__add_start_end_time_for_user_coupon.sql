ALTER TABLE `aa`.`user_coupon` ADD COLUMN `start_time` DATETIME NOT NULL
AFTER `coupon_id`;

ALTER TABLE `aa`.`user_coupon` ADD COLUMN `end_time` DATETIME NOT NULL
AFTER `start_time`;