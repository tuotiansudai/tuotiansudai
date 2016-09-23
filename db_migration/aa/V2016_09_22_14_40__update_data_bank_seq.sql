BEGIN;

UPDATE `bank` SET id = id + 10000;

UPDATE `bank` SET id = 1001, seq = 1 WHERE `bank_code` = 'CCB';
UPDATE `bank` SET id = 1002, seq = 2 WHERE `bank_code` = 'ABC';
UPDATE `bank` SET id = 1003, seq = 3 WHERE `bank_code` = 'ICBC';
UPDATE `bank` SET id = 1004, seq = 4 WHERE `bank_code` = 'BOC';
UPDATE `bank` SET id = 1005, seq = 5 WHERE `bank_code` = 'PSBC';
UPDATE `bank` SET id = 1006, seq = 6 WHERE `bank_code` = 'CMB';
UPDATE `bank` SET id = 1007, seq = 7 WHERE `bank_code` = 'CMBC';
UPDATE `bank` SET id = 1008, seq = 8 WHERE `bank_code` = 'CEB';
UPDATE `bank` SET id = 1009, seq = 9 WHERE `bank_code` = 'COMM';
UPDATE `bank` SET id = 1010, seq = 10 WHERE `bank_code` = 'CIB';
UPDATE `bank` SET id = 1011, seq = 11 WHERE `bank_code` = 'CITIC';
UPDATE `bank` SET id = 1012, seq = 12 WHERE `bank_code` = 'SPDB';
UPDATE `bank` SET id = 1013, seq = 13 WHERE `bank_code` = 'SPAB';
UPDATE `bank` SET id = 1014, seq = 14 WHERE `bank_code` = 'GDB';
UPDATE `bank` SET id = 1015, seq = 15 WHERE `bank_code` = 'HXB';

COMMIT;