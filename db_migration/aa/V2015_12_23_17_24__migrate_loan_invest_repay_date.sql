BEGIN;
UPDATE `aa`.`loan_repay`
SET `repay_date` = CONCAT(extract(YEAR FROM `repay_date`), '-', extract(MONTH FROM `repay_date`), '-', extract(DAY FROM `repay_date`), ' 23:59:59');
UPDATE `aa`.`invest_repay`
SET `repay_date` = CONCAT(extract(YEAR FROM `repay_date`), '-', extract(MONTH FROM `repay_date`), '-', extract(DAY FROM `repay_date`), ' 23:59:59');
COMMIT;