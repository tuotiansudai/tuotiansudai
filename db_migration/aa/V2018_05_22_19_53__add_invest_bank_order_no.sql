ALTER TABLE `aa`.`loan`
ADD COLUMN `bank_order_no` VARCHAR(20) AFTER `id`;

ALTER TABLE `aa`.`loan`
ADD COLUMN `bank_order_date` VARCHAR(8) AFTER `bank_order_no`;
