BEGIN;

UPDATE `bank` SET id = id + 10000;

UPDATE `bank` SET id = 1001 WHERE `bank_code` = 'CCB';
UPDATE `bank` SET id = 1002 WHERE `bank_code` = 'ABC';
UPDATE `bank` SET id = 1003 WHERE `bank_code` = 'ICBC';
UPDATE `bank` SET id = 1004 WHERE `bank_code` = 'BOC';
UPDATE `bank` SET id = 1005 WHERE `bank_code` = 'PSBC';
UPDATE `bank` SET id = 1006 WHERE `bank_code` = 'CMB';
UPDATE `bank` SET id = 1007 WHERE `bank_code` = 'CMBC';
UPDATE `bank` SET id = 1008 WHERE `bank_code` = 'CEB';
UPDATE `bank` SET id = 1009 WHERE `bank_code` = 'COMM';
UPDATE `bank` SET id = 1010 WHERE `bank_code` = 'CIB';
UPDATE `bank` SET id = 1011 WHERE `bank_code` = 'CITIC';
UPDATE `bank` SET id = 1012 WHERE `bank_code` = 'SPDB';
UPDATE `bank` SET id = 1013 WHERE `bank_code` = 'SPAB';
UPDATE `bank` SET id = 1014 WHERE `bank_code` = 'GDB';
UPDATE `bank` SET id = 1015 WHERE `bank_code` = 'HXB';

COMMIT;