BEGIN;

UPDATE loan
SET deadline = (SELECT loan_repay.repay_date
                FROM loan_repay
                WHERE loan_repay.loan_id = loan.id
                ORDER BY period DESC
                LIMIT 1)
WHERE deadline IS NULL;

COMMIT;