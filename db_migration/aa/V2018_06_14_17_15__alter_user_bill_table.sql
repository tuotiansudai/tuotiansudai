ALTER TABLE `aa`.`user_bill`
  ADD COLUMN `bank_order_no` VARCHAR(20) AFTER `order_id`;

ALTER TABLE `aa`.`user_bill`
  ADD COLUMN `bank_order_date` VARCHAR(8) AFTER `bank_order_no`;

