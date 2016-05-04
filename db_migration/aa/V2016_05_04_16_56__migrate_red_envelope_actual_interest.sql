BEGIN;

UPDATE `user_coupon`
SET `actual_interest` = `expected_interest`
WHERE EXISTS(SELECT 1
             FROM coupon
             WHERE coupon.id = user_coupon.coupon_id AND coupon.coupon_type = 'RED_ENVELOPE') AND `status` = 'SUCCESS';

COMMIT;