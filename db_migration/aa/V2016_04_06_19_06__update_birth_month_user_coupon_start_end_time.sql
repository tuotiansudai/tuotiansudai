BEGIN;

UPDATE user_coupon
SET start_time = '2016-03-01 00:00:00', end_time = '2016-03-31 23:59:59'
WHERE
  created_time BETWEEN '2016-03-01 00:00:00' AND '2016-03-31 23:59:59'
  AND
  EXISTS(SELECT 1
         FROM coupon
         WHERE coupon.id = user_coupon.coupon_id AND coupon.coupon_type = 'BIRTHDAY_COUPON');

UPDATE user_coupon
SET start_time = '2016-04-01 00:00:00', end_time = '2016-04-30 23:59:59'
WHERE
  created_time BETWEEN '2016-04-01 00:00:00' AND '2016-04-30 23:59:59'
  AND
  EXISTS(SELECT 1
         FROM coupon
         WHERE coupon.id = user_coupon.coupon_id AND coupon.coupon_type = 'BIRTHDAY_COUPON');

COMMIT;