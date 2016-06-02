BEGIN;

UPDATE loan
SET duration = DATEDIFF((SELECT loan_repay.repay_date
                         FROM loan_repay
                         WHERE loan_repay.period = loan.periods AND loan_repay.loan_id = loan.id), loan.recheck_time)
WHERE loan.recheck_time IS NOT NULL AND loan.status <> 'CANCEL' AND loan.recheck_time < '2015-12-17 23:59:59';

UPDATE loan
SET duration = 1 + DATEDIFF((SELECT loan_repay.repay_date
                             FROM loan_repay
                             WHERE loan_repay.period = loan.periods AND loan_repay.loan_id = loan.id), loan.recheck_time)
WHERE loan.recheck_time IS NOT NULL AND loan.status <> 'CANCEL' AND loan.recheck_time > '2015-12-17 23:59:59';

UPDATE loan
SET duration = loan.periods * 30
WHERE loan.recheck_time IS NULL OR loan.status = 'CANCEL';

COMMIT;

