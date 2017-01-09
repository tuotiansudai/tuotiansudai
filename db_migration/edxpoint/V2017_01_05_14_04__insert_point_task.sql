BEGIN;
INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'REGISTER', '100', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'REGISTER');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'BIND_EMAIL', '50', '0', '0', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'BIND_EMAIL');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'BIND_BANK_CARD', '100', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'BIND_BANK_CARD');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_RECHARGE', '200', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_RECHARGE');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_INVEST', '200', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_INVEST');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'SUM_INVEST_10000', '500', '0', '0', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'SUM_INVEST_10000');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'EACH_SUM_INVEST', '5000', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'EACH_SUM_INVEST');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_SINGLE_INVEST', '10000', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_SINGLE_INVEST');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'EACH_RECOMMEND', '50', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'EACH_RECOMMEND');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'EACH_REFERRER_INVEST', '1000', '0', '0', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'EACH_REFERRER_INVEST');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_REFERRER_INVEST', '50', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_REFERRER_INVEST');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_INVEST_180', '50', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_INVEST_180');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_INVEST_360', '200', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_INVEST_360');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_TURN_ON_NO_PASSWORD_INVEST', '100', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_TURN_ON_NO_PASSWORD_INVEST');

INSERT INTO `point_task` (`name`, `point`, `multiple`, `active`, `max_level`, `created_time`)
  SELECT 'FIRST_TURN_ON_AUTO_INVEST', '50', '0', '1', '1', now() FROM dual
  WHERE NOT EXISTS(SELECT 1 FROM point_task WHERE `name` = 'FIRST_TURN_ON_AUTO_INVEST');
COMMIT;