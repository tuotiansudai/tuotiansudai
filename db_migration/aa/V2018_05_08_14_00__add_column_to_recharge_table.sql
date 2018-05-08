begin;

ALTER TABLE recharge ADD column pay_type VARCHAR(1);
ALTER TABLE recharge ADD column bank_order_no VARCHAR(20);
ALTER TABLE recharge ADD column bank_order_date VARCHAR(8);

COMMIT;