BEGIN;
USE aa;
-- SIGN IN
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

-- TASK
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
COMMIT;