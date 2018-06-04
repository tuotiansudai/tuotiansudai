ALTER TABLE `aa`.`invest_referrer_reward`
  ADD COLUMN `bank_order_no` VARCHAR(20) AFTER `status`;

ALTER TABLE `aa`.`invest_referrer_reward`
  ADD COLUMN `bank_order_date` VARCHAR(8) AFTER `bank_order_no`;
