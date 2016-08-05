BEGIN;
ALTER TABLE `aa`.`coupon`
ADD COLUMN `coupon_source` VARCHAR(50) NOT NULL
AFTER `deleted`,
ADD COLUMN `comment` VARCHAR(100) NULL DEFAULT ''
AFTER `coupon_source`;
COMMIT;