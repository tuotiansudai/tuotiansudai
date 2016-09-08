USE aa;
CREATE TABLE point_bill_temp
  LIKE point_bill;
-- move invest point
INSERT INTO point_bill_temp SELECT *
                            FROM point_bill
                            WHERE business_type = 'INVEST';

-- move sign point
INSERT INTO point_bill_temp SELECT *
                            FROM point_bill
                            WHERE business_type = 'SIGN_IN';
UPDATE point_bill_temp
SET point = 2
WHERE business_type = 'SIGN_IN' AND point = 5;
UPDATE point_bill_temp
SET point = 3
WHERE business_type = 'SIGN_IN' AND point = 10;
UPDATE point_bill_temp
SET point = 4
WHERE business_type = 'SIGN_IN' AND point = 20;
UPDATE point_bill_temp
SET point = 5
WHERE business_type = 'SIGN_IN' AND point = 40;
UPDATE point_bill_temp
SET point = 10
WHERE business_type = 'SIGN_IN' AND point = 80;

-- sign in and invest finish! ready to move task point
CREATE TABLE user_point_task_temp
  LIKE user_point_task;
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
                                   max(created_time)                AS created_time
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
                                   max(created_time)                    AS created_time
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
                                   max(created_time)               AS created_time
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

-- user_point_task finish. ready to fill point_bill
INSERT INTO point_bill_temp SELECT
                              NULL   AS id,
                              NULL   AS order_id,
                              login_name,
                              point,
                              'TASK' AS business_type,
                              CASE task_name
                              WHEN 'REGISTER'
                                THEN '实名认证奖励'
                              WHEN 'BIND_BANK_CARD'
                                THEN '绑定银行卡奖励'
                              WHEN 'FIRST_RECHARGE'
                                THEN '首次充值奖励'
                              WHEN 'FIRST_INVEST'
                                THEN '首次投资奖励'
                              WHEN 'EACH_SUM_INVEST'
                                THEN '累计投资满5000元奖励'
                              WHEN 'FIRST_SINGLE_INVEST'
                                THEN '单笔投资满10000元奖励'
                              WHEN 'EACH_RECOMMEND'
                                THEN '首次邀请好友注册奖励'
                              WHEN 'FIRST_REFERRER_INVEST'
                                THEN '首次邀请好友投资奖励'
                              WHEN 'FIRST_INVEST_180'
                                THEN '首次投资180天标的奖励'
                              WHEN 'FIRST_TURN_ON_NO_PASSWORD_INVEST'
                                THEN '首次开通免密支付奖励'
                              WHEN 'FIRST_TURN_ON_AUTO_INVEST'
                                THEN '首次开通自动投标奖励'
                              WHEN 'FIRST_INVEST_360'
                                THEN '首次投资360天标的奖励'
                              ELSE NULL
                              END
                                     AS note,
                              created_time
                            FROM (SELECT
                                    user_point_task_temp.*,
                                    point_task.name AS task_name
                                  FROM user_point_task_temp
                                    JOIN point_task ON user_point_task_temp.point_task_id = point_task.id) AS temp;

-- delete origin table
DROP TABLE point_bill;
DROP TABLE user_point_task;
-- replace origin table with temp table
ALTER TABLE `aa`.`point_bill_temp` RENAME TO `aa`.`point_bill`;
ALTER TABLE `aa`.`user_point_task_temp` RENAME TO `aa`.`user_point_task`;
-- update account point
-- default point = 0
UPDATE `aa`.`account`
SET point = 0;
-- update
CREATE TABLE tmp (
  login_name VARCHAR(25) PRIMARY KEY,
  point      INT
);
INSERT INTO tmp SELECT
                  login_name,
                  SUM(point)
                FROM point_bill
                GROUP BY login_name;
UPDATE `aa`.`account`, tmp
SET account.point = tmp.point
WHERE tmp.login_name = account.login_name;
DROP TABLE tmp;