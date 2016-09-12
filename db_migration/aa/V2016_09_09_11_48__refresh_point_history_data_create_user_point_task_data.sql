BEGIN;
USE aa;

-- 实名认证 100
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'REGISTER');
UPDATE user_point_task_temp
SET point = 100
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'REGISTER');
-- 绑定银行卡 100
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'BIND_BANK_CARD');
UPDATE user_point_task_temp
SET point = 100
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'BIND_BANK_CARD');
-- 首次充值 200
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_RECHARGE');
UPDATE user_point_task_temp
SET point = 200
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'FIRST_RECHARGE');
-- 首次投资	200
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_INVEST');
UPDATE user_point_task_temp
SET point = 200
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'FIRST_INVEST');
-- 首次投资180天标的奖励	50
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_INVEST_180');
UPDATE user_point_task_temp
SET point = 50
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'FIRST_INVEST_180');
-- 首次开通免密支付	100
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_TURN_ON_NO_PASSWORD_INVEST');
UPDATE user_point_task_temp
SET point = 100
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'FIRST_TURN_ON_NO_PASSWORD_INVEST');
-- 首次开通自动投标	50
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_TURN_ON_AUTO_INVEST');
UPDATE user_point_task_temp
SET point = 50
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'FIRST_TURN_ON_AUTO_INVEST');
-- 首次投资360天标的奖励	200
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_INVEST_360');
UPDATE user_point_task_temp
SET point = 200
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'FIRST_INVEST_360');
-- 累计投资满5000元	100
INSERT INTO user_point_task_temp SELECT
                                   login_name,
                                   (SELECT id
                                    FROM point_task
                                    WHERE name = 'EACH_SUM_INVEST') AS point_task_id,
                                   1                                AS task_level,
                                   100                              AS point,
                                   min(created_time) AS created_time
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'EACH_SUM_INVEST')
                                 GROUP BY login_name;
-- 单笔投资满10000元	200
INSERT INTO user_point_task_temp SELECT
                                   login_name,
                                   (SELECT id
                                    FROM point_task
                                    WHERE name = 'FIRST_SINGLE_INVEST') AS point_task_id,
                                   1                                    AS task_level,
                                   200                                  AS point,
                                   min(created_time) AS created_time
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_SINGLE_INVEST')
                                 GROUP BY login_name;
-- 首次邀请好友注册	50
INSERT INTO user_point_task_temp SELECT
                                   login_name,
                                   (SELECT id
                                    FROM point_task
                                    WHERE name = 'EACH_RECOMMEND') AS point_task_id,
                                   1                               AS task_level,
                                   50                              AS point,
                                   min(created_time) AS created_time
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'EACH_RECOMMEND')
                                 GROUP BY login_name;
-- 首次邀请好友投资	50
INSERT INTO user_point_task_temp SELECT *
                                 FROM user_point_task
                                 WHERE point_task_id = (SELECT id
                                                        FROM point_task
                                                        WHERE name = 'FIRST_REFERRER_INVEST');
UPDATE user_point_task_temp
SET point = 50
WHERE point_task_id = (SELECT id
                       FROM point_task
                       WHERE name = 'FIRST_REFERRER_INVEST');
COMMIT;