BEGIN;

INSERT INTO user_point_task (`login_name`, `point_task_id`, `created_time`, `point`, `task_level`)
  SELECT
    `referrer`,
    9,
    `register_time`,
    200,
    1
  FROM
    aa.`user`
  WHERE `user`.`referrer` IS NOT NULL;


INSERT INTO user_point_task (`login_name`, `point_task_id`, `created_time`, `point`, `task_level`)
  SELECT
    `user`.`referrer`,
    10,
    `invest`.`created_time`,
    1000,
    1
  FROM `aa`.`user`
    JOIN `aa`.`invest` ON `user`.`login_name` = `invest`.`login_name`
  WHERE `user`.`referrer` IS NOT NULL
        AND `invest`.`status` = 'SUCCESS'
        AND `invest`.`loan_id` != 1
        AND `invest`.`transfer_invest_id` IS NULL
        AND `invest`.`amount` >= 100000;


INSERT INTO user_point_task (`login_name`, `point_task_id`, `created_time`, `point`, `task_level`)
  SELECT
    referrer.`login_name`,
    11,
    now(),
    5000,
    1
  FROM `aa`.`user` referrer
  WHERE EXISTS(SELECT 1
               FROM `aa`.`user`
                 JOIN `aa`.`invest` ON `user`.login_name = `invest`.`login_name`
               WHERE `user`.`referrer` = referrer.`login_name`
                     AND `invest`.`status` = 'SUCCESS'
                     AND `invest`.`loan_id` != 1
                     AND `invest`.`transfer_invest_id` IS NULL);


INSERT INTO user_point_task (`login_name`, `point_task_id`, `created_time`, `point`, `task_level`)
  SELECT
    `user`.`login_name`,
    12,
    now(),
    1000,
    1
  FROM `aa`.`user`
  WHERE EXISTS(SELECT 1
               FROM `aa`.`invest`
                 JOIN `aa`.`loan` ON `invest`.`loan_id` = `loan`.`id`
               WHERE `invest`.`login_name` = `user`.`login_name`
                     AND `invest`.`status` = 'SUCCESS'
                     AND `loan`.`product_type` = '_180');


INSERT INTO user_point_task (`login_name`, `point_task_id`, `created_time`, `point`, `task_level`)
  SELECT
    `user`.`login_name`,
    13,
    now(),
    1000,
    1
  FROM `aa`.`user`
  WHERE EXISTS(SELECT 1
               FROM `aa`.`invest`
                 JOIN `aa`.`loan` ON `invest`.`loan_id` = `loan`.`id`
               WHERE `invest`.`login_name` = `user`.`login_name`
                     AND `invest`.`status` = 'SUCCESS'
                     AND `loan`.`product_type` = '_360');


INSERT INTO user_point_task (`login_name`, `point_task_id`, `created_time`, `point`, `task_level`)
  SELECT
    `login_name`,
    14,
    now(),
    1000,
    1
  FROM `aa`.`account`
  WHERE `no_password_invest` IS TRUE;


INSERT INTO user_point_task (`login_name`, `point_task_id`, `created_time`, `point`, `task_level`)
  SELECT
    `login_name`,
    15,
    now(),
    1000,
    1
  FROM
    `aa`.`auto_invest_plan`
  WHERE `enabled` IS TRUE;

COMMIT;