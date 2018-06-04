ALTER TABLE `aa`.`loan`
ADD COLUMN `loan_tx_no` VARCHAR(32);

ALTER TABLE `aa`.`loan`
ADD COLUMN `loan_acc_no` VARCHAR(32);

ALTER TABLE `aa`.`loan`
ADD COLUMN `bank_order_no` VARCHAR(20);

ALTER TABLE `aa`.`loan`
ADD COLUMN `bank_order_date` VARCHAR(8);

ALTER TABLE `aa`.`loan`
ADD COLUMN `loan_full_bank_order_no` VARCHAR(20);

ALTER TABLE `aa`.`loan`
ADD COLUMN `loan_full_bank_order_date` VARCHAR(8);
