ALTER TABLE `edxactivity`.`user_lottery_prize` 
ADD COLUMN `prize_type` VARCHAR(50) NOT NULL AFTER `lottery_time`;