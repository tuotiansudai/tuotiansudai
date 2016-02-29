ALTER TABLE `aa`.`user_coupon` ADD COLUMN `start_time` DATETIME NOT NULL
AFTER `coupon_id`;

ALTER TABLE `aa`.`user_coupon` ADD COLUMN `end_time` DATETIME NOT NULL
AFTER `start_time`;

UPDATE user_coupon
SET start_time = (SELECT coupon.start_time
                  FROM coupon
                  WHERE user_coupon.coupon_id = coupon.id),
  end_time     = (SELECT coupon.end_time
                  FROM coupon
                  WHERE user_coupon.coupon_id = coupon.id)