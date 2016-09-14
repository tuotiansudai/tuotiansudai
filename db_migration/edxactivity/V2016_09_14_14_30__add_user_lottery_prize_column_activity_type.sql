ALTER TABLE `edxactivity`.`user_lottery_prize` 
ADD COLUMN `prize_type` VARCHAR(10) NOT NULL AFTER `lottery_time`;