BEGIN;
# 累计投资满5000元奖励1000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    1000,
    now(),
    'TASK',
    '累计投资满5000元奖励1000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 500000;

# 累计投资满10000元奖励2000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    2000,
    now(),
    'TASK',
    '累计投资满10000元奖励2000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 1000000;

# 累计投资满50000元奖励5000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    5000,
    now(),
    'TASK',
    '累计投资满50000元奖励5000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 5000000;

# 累计投资满100000元奖励10000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    10000,
    now(),
    'TASK',
    '累计投资满100000元奖励10000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 10000000;

# 累计投资满500000元奖励50000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    50000,
    now(),
    'TASK',
    '累计投资满500000元奖励50000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 50000000;

# 累计投资满1000000元奖励100000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    100000,
    now(),
    'TASK',
    '累计投资满1000000元奖励100000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 100000000;

# 累计投资满2000000元奖励200000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    200000,
    now(),
    'TASK',
    '累计投资满2000000元奖励200000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 200000000;

# 累计投资满3000000元奖励300000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    300000,
    now(),
    'TASK',
    '累计投资满3000000元奖励300000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 300000000;

# 累计投资满4000000元奖励400000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    400000,
    now(),
    'TASK',
    '累计投资满4000000元奖励400000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 400000000;

# 累计投资满5000000元奖励500000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    500000,
    now(),
    'TASK',
    '累计投资满5000000元奖励500000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 500000000;

# 累计投资满6000000元奖励600000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    7,
    600000,
    now(),
    'TASK',
    '累计投资满6000000元奖励600000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 600000000;

# 单笔投资满10000元奖励2000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    8,
    2000,
    now(),
    'TASK',
    '单笔投资满10000元奖励2000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 1000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满50000元奖励5000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    8,
    5000,
    now(),
    'TASK',
    '单笔投资满50000元奖励5000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 5000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满100000元奖励10000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    8,
    10000,
    now(),
    'TASK',
    '单笔投资满100000元奖励10000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 10000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满200000元奖励20000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    8,
    20000,
    now(),
    'TASK',
    '单笔投资满200000元奖励20000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 20000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满500000元奖励50000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    8,
    50000,
    now(),
    'TASK',
    '单笔投资满500000元奖励50000财豆'
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 50000000
  GROUP BY `invest`.`login_name`;

# 每邀请1名好友注册奖励200财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `referrer_relation`.`referrer_login_name`,
    9,
    200,
    now(),
    'TASK',
    '每邀请1名好友注册奖励200财豆'
  FROM `referrer_relation`
  WHERE `referrer_relation`.`level` = 1
        AND EXISTS(SELECT 1
                   FROM `account`
                   WHERE `account`.`login_name` = `referrer_relation`.`referrer_login_name`);

# 每邀请好友投资满1000元奖励1000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `referrer_relation`.`referrer_login_name`,
    10,
    1000,
    now(),
    'TASK',
    '每邀请好友投资满1000元奖励1000财豆'
  FROM `referrer_relation`
    JOIN `invest` ON `invest`.`login_name` = `referrer_relation`.`login_name` AND `invest`.`status` = 'SUCCESS' AND `invest`.`amount` >= 100000 AND `invest`.`transfer_invest_id` IS NULL
  WHERE `referrer_relation`.`level` = 1
        AND EXISTS(SELECT 1
                   FROM `account`
                   WHERE `account`.`login_name` = `referrer_relation`.`referrer_login_name`);

# 首次邀请好友投资奖励5000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `referrer_relation`.`referrer_login_name`,
    11,
    5000,
    now(),
    'TASK',
    '首次邀请好友投资奖励5000财豆'
  FROM `referrer_relation`
  WHERE `referrer_relation`.`level` = 1
        AND EXISTS(SELECT 1
                   FROM `account`
                   WHERE `account`.`login_name` = `referrer_relation`.`referrer_login_name`)
        AND EXISTS(SELECT 1
                   FROM `invest`
                   WHERE `invest`.`login_name` = `referrer_relation`.`login_name` AND `invest`.`status` = 'SUCCESS' AND `invest`.`transfer_invest_id` IS NULL)
  GROUP BY `referrer_relation`.`referrer_login_name`;

# 首次投资180天标的奖励1000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    12,
    1000,
    now(),
    'TASK',
    '首次投资180天标的奖励1000财豆'
  FROM `invest`
    JOIN `loan` ON `invest`.`loan_id` = `loan`.id
  WHERE `invest`.`status` = 'SUCCESS' AND `loan`.`product_type` = '_180' AND `invest`.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`;

# 首次投资360天标的奖励1000财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `invest`.`login_name`,
    13,
    1000,
    now(),
    'TASK',
    '首次投资360天标的奖励1000财豆'
  FROM `invest`
    JOIN `loan` ON `invest`.`loan_id` = `loan`.id
  WHERE `invest`.`status` = 'SUCCESS' AND `loan`.`product_type` = '_360' AND `invest`.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`;

# 首次开通免密支付奖励500财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `account`.`login_name`,
    14,
    500,
    now(),
    'TASK',
    '首次开通免密支付奖励500财豆'
  FROM `account`
  WHERE `account`.`no_password_invest` IS TRUE;

# 首次开通自动投标奖励500财豆
INSERT INTO `point_bill` (`login_name`, `order_id`, `point`, `created_time`, `business_type`, `note`)
  SELECT
    `auto_invest_plan`.`login_name`,
    15,
    500,
    now(),
    'TASK',
    '首次开通自动投标奖励500财豆'
  FROM `auto_invest_plan`
  WHERE `auto_invest_plan`.`enabled` IS TRUE;

COMMIT;