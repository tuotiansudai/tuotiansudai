BEGIN;
UPDATE `bank_card` SET `bank_no` = 'BJB' WHERE `bank_no` = 'BJBANK';

UPDATE `bank_card` SET `bank_no` = 'CZSB' WHERE `bank_no` = 'CZB';

UPDATE `bank_card` SET `bank_no` = 'SHNS' WHERE `bank_no` = 'SHRCB';

UPDATE `bank_card` SET `bank_no` = 'SPAB' WHERE `bank_no` = 'SZPAB';
COMMIT;