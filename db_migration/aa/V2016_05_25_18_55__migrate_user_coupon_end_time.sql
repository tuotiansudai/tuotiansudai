BEGIN;

UPDATE user_coupon
  JOIN coupon ON user_coupon.coupon_id = coupon.id
SET user_coupon.end_time = coupon.end_time
WHERE coupon.end_time >= now()
AND coupon.deadline = 0;

COMMIT;