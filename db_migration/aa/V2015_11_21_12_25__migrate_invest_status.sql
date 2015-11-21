BEGIN;

UPDATE `aa`.`invest`
SET `status` = 'WAIT_PAY'
WHERE `status` = 'WAITING';

COMMIT;