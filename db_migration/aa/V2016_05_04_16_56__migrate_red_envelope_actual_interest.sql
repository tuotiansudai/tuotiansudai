BEGIN;

UPDATE `user_coupon`
SET `actual_interest` = `expected_interest`
WHERE `status` = 'SUCCESS';

COMMIT;