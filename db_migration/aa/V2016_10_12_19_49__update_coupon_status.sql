BEGIN;

UPDATE coupon_repay
SET
    status = 'REPAYING'
WHERE
    actual_repay_date IS NULL
        OR repay_date < actual_repay_date;

COMMIT;