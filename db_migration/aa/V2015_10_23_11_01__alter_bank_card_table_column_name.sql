ALTER TABLE `aa`.`bank_card` CHANGE COLUMN `is_open_fastPayment` `is_fast_pay_on` BOOLEAN DEFAULT FALSE;
ALTER TABLE `aa`.`bank_card` CHANGE COLUMN `bank_number` `bank_code` VARCHAR(8);
ALTER TABLE `aa`.`recharge` CHANGE COLUMN `bank` `bank_code` VARCHAR(8);