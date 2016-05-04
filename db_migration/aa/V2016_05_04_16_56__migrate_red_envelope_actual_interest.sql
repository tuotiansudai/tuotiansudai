BEGIN;

UPDATE `user_coupon`
SET `actual_interest` = `expected_interest`
WHERE coupon_type = 'RED_ENVELOPE' and `status` = 'SUCCESS';

COMMIT;