BEGIN;

UPDATE `bank` SET seq = 1 WHERE `bank_code` = 'CCB';
UPDATE `bank` SET seq = 2 WHERE `bank_code` = 'ICBC';
UPDATE `bank` SET seq = 3 WHERE `bank_code` = 'ABC';
UPDATE `bank` SET seq = 4 WHERE `bank_code` = 'BOC';
UPDATE `bank` SET seq = 5 WHERE `bank_code` = 'CMBC';
UPDATE `bank` SET seq = 6 WHERE `bank_code` = 'CEB';
UPDATE `bank` SET seq = 7 WHERE `bank_code` = 'COMM';
UPDATE `bank` SET seq = 8 WHERE `bank_code` = 'CIB';
UPDATE `bank` SET seq = 9 WHERE `bank_code` = 'CITIC';
UPDATE `bank` SET seq = 10 WHERE `bank_code` = 'SPDB';
UPDATE `bank` SET seq = 11 WHERE `bank_code` = 'SPAB';
UPDATE `bank` SET seq = 12 WHERE `bank_code` = 'GDB';
UPDATE `bank` SET seq = 13 WHERE `bank_code` = 'CMB';
UPDATE `bank` SET seq = 14 WHERE `bank_code` = 'PSBC';
UPDATE `bank` SET seq = 15 WHERE `bank_code` = 'HXB';

COMMIT;