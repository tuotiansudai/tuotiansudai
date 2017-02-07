BEGIN;

UPDATE `point_task` SET `point` = 100, `multiple` = 1, `max_level` = 0 WHERE `name` = 'EACH_RECOMMEND';

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  VALUES ('EACH_RECOMMEND_BANK_CARD', '100', '1', '1', '0', now());

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  VALUES ('EACH_RECOMMEND_INVEST', '200', '1', '1', '0', now());

COMMIT;