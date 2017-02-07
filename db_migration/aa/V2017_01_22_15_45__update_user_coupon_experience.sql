BEGIN;

UPDATE user_coupon
SET coupon_id = 382
WHERE coupon_id = 100035 AND (status != 'SUCCESS' OR status IS NULL);

UPDATE coupon
SET issued_count = (SELECT count(1)
                    FROM user_coupon
                    WHERE coupon_id = 382)
WHERE id = 382;

UPDATE coupon
SET issued_count = (SELECT count(1)
                    FROM user_coupon
                    WHERE coupon_id = 100035)
WHERE id = 100035;

COMMIT;