alter table bank_card add column(is_open_fastPayment varchar(2)) ;
BEGIN;
UPDATE `bank_card` set `is_open_fastPayment` = '0';
COMMIT;