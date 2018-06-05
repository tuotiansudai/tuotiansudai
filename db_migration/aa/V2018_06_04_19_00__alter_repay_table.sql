ALTER TABLE `aa`.`loan_repay`
  ADD COLUMN `bank_order_no` VARCHAR(20);

ALTER TABLE `aa`.`loan_repay`
  ADD COLUMN `bank_order_date` VARCHAR(8);

  ALTER TABLE `aa`.`invest_repay`
  ADD COLUMN `bank_order_no` VARCHAR(20);

ALTER TABLE `aa`.`invest_repay`
  ADD COLUMN `bank_order_date` VARCHAR(8);
