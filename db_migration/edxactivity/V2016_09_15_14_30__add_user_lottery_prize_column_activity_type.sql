ALTER TABLE `edxactivity`.`user_lottery_prize` 
ADD COLUMN `activity_category` VARCHAR(50) NOT NULL AFTER `lottery_time`;