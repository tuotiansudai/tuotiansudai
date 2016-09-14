ALTER TABLE `edxactivity`.`user_lottery_prize` 
ADD COLUMN `prize_type` VARCHAR(50) NOT NULL AFTER `lottery_time`;

UPDATE `edxactivity`.`user_lottery_prize` SET prize_type = 'AUTUMN_PRIZE' WHERE prize_type IS NULL OR prize_type = '';

