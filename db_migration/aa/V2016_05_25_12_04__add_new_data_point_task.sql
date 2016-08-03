BEGIN;
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (7, 'EACH_SUM_INVEST', 5000, now(), 1, 1, 0);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (8, 'FIRST_SINGLE_INVEST', 10000, now(), 1, 1, 5);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (9, 'EACH_RECOMMEND', 200, now(), 1, 1, 0);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (10, 'EACH_REFERRER_INVEST', 1000, now(), 1, 1, 0);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (11, 'FIRST_REFERRER_INVEST', 5000, now(), 1, 0, 1);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (12, 'FIRST_INVEST_180', 1000, now(), 1, 0, 1);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (13, 'FIRST_INVEST_360', 1000, now(), 1, 0, 1);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (14, 'FIRST_TURN_ON_NO_PASSWORD_INVEST', 1000, now(), 1, 0, 1);
INSERT INTO `aa`.`point_task` (`id`, `name`, `point`, `created_time`, `active`, `multiple`, `max_level`) VALUES (15, 'FIRST_TURN_ON_AUTO_INVEST', 1000, now(), 1, 0, 1);

UPDATE `aa`.`point_task` SET `point` = 200 WHERE `name` = 'REGISTER';
UPDATE `aa`.`point_task` SET `point` = 200 WHERE `name` = 'BIND_BANK_CARD';
UPDATE `aa`.`point_task` SET `point` = 500 WHERE `name` = 'FIRST_RECHARGE';
UPDATE `aa`.`point_task` SET `point` = 1000 WHERE `name` = 'FIRST_INVEST';

UPDATE `aa`.`point_task`
SET active = FALSE
WHERE `name` IN ('BIND_EMAIL', 'SUM_INVEST_10000');

UPDATE `user_point_task`
SET `user_point_task`.`point` = (SELECT `point_task`.`point`
                                 FROM `point_task`
                                 WHERE `point_task`.`id` = `user_point_task`.`point_task_id`);
COMMIT;