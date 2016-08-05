BEGIN;
ALTER TABLE `aa`.`coupon`
ADD COLUMN `coupon_source` VARCHAR(50) NOT NULL
AFTER `deleted`,
ADD COLUMN `comment` VARCHAR(100) NULL DEFAULT ''
AFTER `coupon_source`;

UPDATE `aa`.`coupon`
SET `coupon_source` = '拓天速贷平台赠送';
COMMIT;