begin;
UPDATE `edxactivity`.`user_lottery_prize` SET activity_category = 'AUTUMN_PRIZE' WHERE activity_category IS NULL OR activity_category = '';
commit;

