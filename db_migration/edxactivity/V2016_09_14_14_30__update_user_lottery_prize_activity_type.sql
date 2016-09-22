begin;
UPDATE `edxactivity`.`user_lottery_prize` SET prize_type = 'AUTUMN_PRIZE' WHERE prize_type IS NULL OR prize_type = '';
commit;

