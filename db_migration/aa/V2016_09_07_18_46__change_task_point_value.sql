BEGIN;
-- change task point
UPDATE `aa`.`point_task`
SET point = 100
WHERE name = 'REGISTER';
UPDATE `aa`.`point_task`
SET point = 100
WHERE name = 'BIND_BANK_CARD';
UPDATE `aa`.`point_task`
SET point = 200
WHERE name = 'FIRST_RECHARGE';
UPDATE `aa`.`point_task`
SET point = 200
WHERE name = 'FIRST_INVEST';
UPDATE `aa`.`point_task`
SET point = 50
WHERE name = 'FIRST_INVEST_180';
UPDATE `aa`.`point_task`
SET point = 100
WHERE name = 'FIRST_TURN_ON_NO_PASSWORD_INVEST';
UPDATE `aa`.`point_task`
SET point = 50
WHERE name = 'FIRST_TURN_ON_AUTO_INVEST';
UPDATE `aa`.`point_task`
SET point = 200
WHERE name = 'FIRST_INVEST_360';
UPDATE `aa`.`point_task`
SET point = 50
WHERE name = 'FIRST_REFERRER_INVEST';
UPDATE `aa`.`point_task`
SET point = 50
WHERE name = 'EACH_RECOMMEND';
-- modify multiple task detail
UPDATE `aa`.`point_task`
SET multiple = 0
WHERE name IN ('EACH_SUM_INVEST', 'FIRST_SINGLE_INVEST', 'EACH_RECOMMEND', 'EACH_REFERRER_INVEST');
UPDATE `aa`.`point_task`
SET max_level = 1
WHERE name IN ('EACH_SUM_INVEST', 'FIRST_SINGLE_INVEST', 'EACH_RECOMMEND', 'EACH_REFERRER_INVEST');
UPDATE `aa`.`point_task`
SET active = 0
WHERE name = 'EACH_REFERRER_INVEST';

COMMIT;