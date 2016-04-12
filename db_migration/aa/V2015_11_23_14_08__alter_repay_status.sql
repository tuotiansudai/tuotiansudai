BEGIN;

UPDATE `aa`.`loan_repay` SET `status`='WAIT_PAY' WHERE `status`='CONFIRMING';

COMMIT;