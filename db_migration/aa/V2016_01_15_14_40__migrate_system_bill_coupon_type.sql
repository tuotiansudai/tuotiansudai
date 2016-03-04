BEGIN;
UPDATE `aa`.`system_bill`
SET business_type = 'COUPON'
WHERE business_type = 'NEWBIE_COUPON';

UPDATE `aa`.user_bill
SET `business_type` = (SELECT `coupon`.`coupon_type`
                       FROM `user_coupon`
                         JOIN `coupon` ON `user_coupon`.`coupon_id` = `coupon`.`id`
                       WHERE `user_coupon`.`id` = `user_bill`.`order_id`)
WHERE `business_type` = 'NEWBIE_COUPON';

COMMIT;