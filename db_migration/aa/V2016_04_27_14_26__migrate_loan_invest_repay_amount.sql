BEGIN;

UPDATE loan_repay
SET repay_amount = corpus + actual_interest + default_interest
WHERE status = 'COMPLETE';

UPDATE invest_repay
SET repay_amount = corpus + actual_interest + default_interest - actual_fee
WHERE status = 'COMPLETE';

COMMIT;