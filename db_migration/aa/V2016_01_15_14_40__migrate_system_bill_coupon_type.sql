BEGIN;
UPDATE `aa`.`system_bill`
SET business_type = 'COUPON'
WHERE business_type = 'NEWBIE_COUPON';

UPDATE `aa`.user_bill
SET `business_type` = (SELECT `coupon_type`
                       FROM `coupon`
                       WHERE id = `user_bill`.`order_id`)
WHERE `business_type` = 'NEWBIE_COUPON';

COMMIT;