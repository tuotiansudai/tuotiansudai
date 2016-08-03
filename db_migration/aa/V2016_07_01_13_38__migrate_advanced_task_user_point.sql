BEGIN;

# 累计投资满5000元奖励1000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 1000
WHERE t.`amount` >= 500000;

# 累计投资满10000元奖励2000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 2000
WHERE t.`amount` >= 1000000;

# 累计投资满50000元奖励5000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 5000
WHERE t.`amount` >= 5000000;

# 累计投资满100000元奖励10000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 10000
WHERE t.`amount` >= 10000000;

# 累计投资满500000元奖励50000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 50000
WHERE t.`amount` >= 50000000;

# 累计投资满1000000元奖励100000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 100000
WHERE t.`amount` >= 100000000;

# 累计投资满2000000元奖励200000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 200000
WHERE t.`amount` >= 200000000;

# 累计投资满3000000元奖励300000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 300000
WHERE t.`amount` >= 300000000;

# 累计投资满4000000元奖励400000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 400000
WHERE t.`amount` >= 400000000;

# 累计投资满5000000元奖励500000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 500000
WHERE t.`amount` >= 500000000;

# 累计投资满6000000元奖励600000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          sum(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 600000
WHERE t.`amount` >= 600000000;

# 单笔投资满10000元奖励2000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          max(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 2000
WHERE t.`amount` >= 1000000;

# 单笔投资满50000元奖励5000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          max(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 5000
WHERE t.`amount` >= 5000000;

# 单笔投资满100000元奖励10000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          max(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 10000
WHERE t.`amount` >= 10000000;

# 单笔投资满200000元奖励20000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          max(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 20000
WHERE t.`amount` >= 20000000;

# 单笔投资满500000元奖励50000财豆
UPDATE `account`
  JOIN (SELECT
          `login_name` AS `login_name`,
          max(amount)  AS `amount`
        FROM `invest`
        WHERE `invest`.`status` = 'SUCCESS'
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 50000
WHERE t.`amount` >= 50000000;

# 每邀请1名好友注册奖励200财豆
UPDATE `account`
  JOIN (SELECT
          `referrer_relation`.`referrer_login_name` AS `login_name`,
          count(1)                                  AS `count`
        FROM `referrer_relation`
        WHERE `referrer_relation`.`level` = 1
        GROUP BY `referrer_relation`.`referrer_login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + (t.`count` * 200);

# 首次邀请好友投资奖励5000财豆
UPDATE `account`
  JOIN (SELECT `referrer_relation`.`referrer_login_name` AS `login_name`
        FROM `referrer_relation`
        WHERE `referrer_relation`.`level` = 1
              AND EXISTS(SELECT 1
                         FROM `invest`
                         WHERE `invest`.`login_name` = `referrer_relation`.`login_name` AND `invest`.`status` = 'SUCCESS' AND `invest`.`transfer_invest_id` IS NULL)
        GROUP BY `referrer_relation`.`referrer_login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 5000;

# 每邀请好友投资满1000元奖励1000财豆
UPDATE `account`
  JOIN (SELECT
          `referrer_relation`.`referrer_login_name` AS `login_name`,
          count(1)                                  AS `count`
        FROM `referrer_relation`
          JOIN `invest` ON `referrer_relation`.`login_name` = `invest`.`login_name`
        WHERE `referrer_relation`.`level` = 1 AND `invest`.`status` = 'SUCCESS' AND `invest`.`amount` >= 100000 AND `invest`.`transfer_invest_id` IS NULL
        GROUP BY `referrer_relation`.`referrer_login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + t.`count` * 1000;

# 首次投资180天标的奖励1000财豆
UPDATE `account`
  JOIN (SELECT `invest`.`login_name` AS `login_name`
        FROM `invest`
          JOIN `loan` ON `invest`.`loan_id` = `loan`.id
        WHERE `invest`.`status` = 'SUCCESS' AND `loan`.`product_type` = '_180' AND `invest`.`transfer_invest_id` IS NULL
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 1000;

# 首次投资360天标的奖励1000财豆
UPDATE `account`
  JOIN (SELECT `invest`.`login_name` AS `login_name`
        FROM `invest`
          JOIN `loan` ON `invest`.`loan_id` = `loan`.id
        WHERE `invest`.`status` = 'SUCCESS' AND `loan`.`product_type` = '_360' AND `invest`.`transfer_invest_id` IS NULL
        GROUP BY `invest`.`login_name`) t
    ON `account`.`login_name` = t.`login_name`
SET `account`.`point` = `account`.`point` + 1000;

# 首次开通免密支付奖励500财豆
UPDATE `account`
SET `account`.`point` = `account`.`point` + 500
WHERE `account`.`no_password_invest` IS TRUE;

# 首次开通自动投标奖励500财豆
UPDATE `account`
  JOIN `auto_invest_plan` ON `account`.`login_name` = `auto_invest_plan`.`login_name`
SET `account`.`point` = `account`.`point` + 500
WHERE `auto_invest_plan`.`enabled` IS TRUE;

COMMIT;