BEGIN;

# 首次开通自动投标奖励500财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `auto_invest_plan`.`login_name`,
    15,
    1,
    500,
    now()
  FROM `auto_invest_plan`
  WHERE `auto_invest_plan`.`enabled` IS TRUE;

# 首次开通免密支付奖励500财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `account`.`login_name`,
    14,
    1,
    500,
    now()
  FROM `account`
  WHERE `account`.`no_password_invest` IS TRUE;

# 首次投资360天标的奖励1000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    13,
    1,
    1000,
    now()
  FROM `invest`
    JOIN `loan` ON `invest`.`loan_id` = `loan`.id
  WHERE `invest`.`status` = 'SUCCESS' AND `loan`.`product_type` = '_360' AND `invest`.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`;

# 首次投资180天标的奖励1000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    12,
    1,
    1000,
    now()
  FROM `invest`
    JOIN `loan` ON `invest`.`loan_id` = `loan`.id
  WHERE `invest`.`status` = 'SUCCESS' AND `loan`.`product_type` = '_180' AND `invest`.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`;

# 首次邀请好友投资奖励5000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `referrer_relation`.`referrer_login_name`,
    11,
    1,
    5000,
    now()
  FROM `referrer_relation`
  WHERE `referrer_relation`.`level` = 1
        AND EXISTS(SELECT 1
                   FROM `account`
                   WHERE `account`.`login_name` = `referrer_relation`.`referrer_login_name`)
        AND EXISTS(SELECT 1
                   FROM `invest`
                   WHERE `invest`.`login_name` = `referrer_relation`.`login_name` AND `invest`.`status` = 'SUCCESS' AND `invest`.`transfer_invest_id` IS NULL)
  GROUP BY `referrer_relation`.`referrer_login_name`;

# 每邀请好友投资满1000元奖励1000财豆
SET @task_level := 0;
SET @temp_login_name := '';
INSERT INTO `user_point_task` (`point_task_id`, `task_level`, `point`, `created_time`, `login_name`)
  SELECT
    `t`.`point_task_id`,
    CASE
    WHEN @temp_login_name = `t`.`login_name`
      THEN @task_level := @task_level + 1
    ELSE @task_level := 1
    END AS task_level,
    `t`.`point`,
    `t`.`created_time`,
    @temp_login_name := `t`.`login_name`
  FROM (
         SELECT
           10                                        AS `point_task_id`,
           1000                                      AS `point`,
           now()                                     AS `created_time`,
           `referrer_relation`.`referrer_login_name` AS `login_name`
         FROM `referrer_relation`
           JOIN `invest` ON `invest`.`login_name` = `referrer_relation`.`login_name` AND `invest`.`status` = 'SUCCESS' AND `invest`.`amount` >= 100000 AND `invest`.`transfer_invest_id` IS NULL
         WHERE `referrer_relation`.`level` = 1
               AND EXISTS(SELECT 1
                          FROM `account`
                          WHERE `account`.`login_name` = `referrer_relation`.`referrer_login_name`)
         ORDER BY `referrer_relation`.`referrer_login_name`
       ) t;

# 每邀请1名好友注册奖励200财豆
SET @task_level := 0;
SET @temp_login_name := '';
INSERT INTO `user_point_task` (`point_task_id`, `task_level`, `point`, `created_time`, `login_name`)
  SELECT
    `t`.`point_task_id`,
    CASE
    WHEN @temp_login_name = `t`.`login_name`
      THEN @task_level := @task_level + 1
    ELSE @task_level := 1
    END AS task_level,
    `t`.`point`,
    `t`.`created_time`,
    @temp_login_name := `t`.`login_name`
  FROM (
         SELECT
           9                                         AS `point_task_id`,
           200                                       AS `point`,
           now()                                     AS `created_time`,
           `referrer_relation`.`referrer_login_name` AS `login_name`
         FROM `referrer_relation`
         WHERE `referrer_relation`.`level` = 1
               AND EXISTS(SELECT 1
                          FROM `account`
                          WHERE `account`.`login_name` = `referrer_relation`.`referrer_login_name`)
         ORDER BY `referrer_relation`.`referrer_login_name`
       ) t;

# 单笔投资满10000元奖励2000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    8,
    1,
    2000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 1000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满50000元奖励5000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    8,
    2,
    5000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 5000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满100000元奖励10000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    8,
    3,
    10000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 10000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满200000元奖励20000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    8,
    4,
    20000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 20000000
  GROUP BY `invest`.`login_name`;

# 单笔投资满500000元奖励50000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    8,
    5,
    50000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL AND `invest`.`amount` >= 50000000
  GROUP BY `invest`.`login_name`;

# 累计投资满5000元奖励1000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    1,
    1000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 500000;

# 累计投资满10000元奖励2000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    2,
    2000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 1000000;

# 累计投资满50000元奖励5000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    3,
    5000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 5000000;

# 累计投资满100000元奖励10000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    4,
    10000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 10000000;

# 累计投资满500000元奖励50000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    5,
    50000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 50000000;

# 累计投资满1000000元奖励100000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    6,
    100000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 100000000;

# 累计投资满2000000元奖励200000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    7,
    200000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 200000000;

# 累计投资满3000000元奖励300000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    8,
    300000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 300000000;

# 累计投资满4000000元奖励400000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    9,
    400000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 400000000;

# 累计投资满5000000元奖励500000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    10,
    500000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 500000000;

# 累计投资满6000000元奖励600000财豆
INSERT INTO `user_point_task` (`login_name`, `point_task_id`, `task_level`, `point`, `created_time`)
  SELECT
    `invest`.`login_name`,
    7,
    11,
    600000,
    now()
  FROM `invest`
  WHERE `invest`.`status` = 'SUCCESS' AND invest.`transfer_invest_id` IS NULL
  GROUP BY `invest`.`login_name`
  HAVING sum(`invest`.`amount`) >= 600000000;

COMMIT;