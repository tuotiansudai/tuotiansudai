BEGIN;

# migrate INVEST_COUPON
DELETE FROM user_point_prize
WHERE point_prize_id = 100006;
DELETE FROM point_prize
WHERE coupon_id = 302;
DELETE FROM user_coupon
WHERE coupon_id IN (302, 100004);

DELETE FROM `coupon`
WHERE `coupon`.`id` IN (302, 100004);

# migrate INTEREST_COUPON
INSERT INTO coupon_repay (`login_name`,
                          `coupon_id`,
                          `user_coupon_id`,
                          `invest_id`,
                          `period`,
                          `expected_interest`,
                          `expected_fee`,
                          `actual_interest`,
                          `actual_fee`,
                          `repay_amount`,
                          `repay_date`,
                          `actual_repay_date`,
                          `is_transferred`,
                          `status`,
                          `created_time`)

  (SELECT
     `invest`.`login_name`                                  AS `login_name`,
     `coupon`.`id`                                          AS `coupon_id`,
     `user_coupon`.`id`                                     AS `user_coupon_id`,
     `invest`.`id`                                          AS `invest_id`,
     `invest_repay`.`period`                                AS `period`,
     floor(CASE
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'INVEST_INTEREST_MONTHLY_REPAY'
             THEN datediff(`invest_repay`.`repay_date`, `invest`.`created_time`) + 1
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'LOAN_INTEREST_MONTHLY_REPAY'
             THEN datediff(`invest_repay`.`repay_date`, `loan`.`recheck_time`) + 1
           ELSE
             datediff(`invest_repay`.`repay_date`, (SELECT `last_repay`.`repay_date`
                                                    FROM `invest_repay` `last_repay`
                                                    WHERE `last_repay`.`invest_id` = `invest_repay`.`invest_id` AND
                                                          `last_repay`.`period` = `invest_repay`.`period` - 1))
           END * `coupon`.`rate` * `invest`.`amount` / 365) AS `expected_interest`,
     0                                                      AS expected_fee,
     floor(CASE
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'INVEST_INTEREST_MONTHLY_REPAY' AND
                `invest_repay`.`status` = 'COMPLETE' AND `invest_repay`.`is_transferred` IS FALSE
             THEN datediff(`invest_repay`.`actual_repay_date`, `invest`.`created_time`) + 1
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'LOAN_INTEREST_MONTHLY_REPAY' AND
                `invest_repay`.`status` = 'COMPLETE' AND `invest_repay`.`is_transferred` IS FALSE
             THEN datediff(`invest_repay`.`actual_repay_date`, `loan`.`recheck_time`) + 1
           WHEN `invest_repay`.`period` != 1 AND `invest_repay`.`status` = 'COMPLETE' AND
                `invest_repay`.`is_transferred` IS FALSE
             THEN datediff(`invest_repay`.`actual_repay_date`, (SELECT `last_repay`.`actual_repay_date`
                                                                FROM `invest_repay` `last_repay`
                                                                WHERE
                                                                  `last_repay`.`invest_id` = `invest_repay`.`invest_id`
                                                                  AND
                                                                  `last_repay`.`period` = `invest_repay`.`period` - 1))
           ELSE 0
           END * `coupon`.`rate` * `invest`.`amount` / 365) AS `actual_interest`,
     0                                                      AS actual_fee,
     0                                                      AS repay_amount,
     `invest_repay`.`repay_date`                            AS `repay_date`,
     `invest_repay`.`actual_repay_date`                     AS `actual_repay_date`,
     `invest_repay`.`is_transferred`                        AS `is_transferred`,
     `invest_repay`.`status`                                AS `status`,
     `invest_repay`.`created_time`                          AS `created_time`
   FROM `user_coupon`
     JOIN `coupon` ON `user_coupon`.`coupon_id` = `coupon`.id
     JOIN invest ON user_coupon.invest_id = invest.id
     JOIN `invest_repay` ON `user_coupon`.`invest_id` = `invest_repay`.`invest_id`
     JOIN loan ON user_coupon.loan_id = loan.id
   WHERE `coupon`.`coupon_type` = 'INTEREST_COUPON' AND `user_coupon`.`status` = 'SUCCESS');

# migrate NEWBIE_COUPON
INSERT INTO coupon_repay (`login_name`,
                          `coupon_id`,
                          `user_coupon_id`,
                          `invest_id`,
                          `period`,
                          `expected_interest`,
                          `expected_fee`,
                          `actual_interest`,
                          `actual_fee`,
                          `repay_amount`,
                          `repay_date`,
                          `actual_repay_date`,
                          `is_transferred`,
                          `status`,
                          `created_time`)

  (SELECT
     `invest`.`login_name`                                                                AS `login_name`,
     `coupon`.`id`                                                                        AS `coupon_id`,
     `user_coupon`.`id`                                                                   AS `user_coupon_id`,
     `invest`.`id`                                                                        AS `invest_id`,
     `invest_repay`.`period`                                                              AS `period`,
     floor(CASE
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'INVEST_INTEREST_MONTHLY_REPAY'
             THEN datediff(`invest_repay`.`repay_date`, `invest`.`created_time`) + 1
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'LOAN_INTEREST_MONTHLY_REPAY'
             THEN datediff(`invest_repay`.`repay_date`, `loan`.`recheck_time`) + 1
           ELSE
             datediff(`invest_repay`.`repay_date`, (SELECT `last_repay`.`repay_date`
                                                    FROM `invest_repay` `last_repay`
                                                    WHERE `last_repay`.`invest_id` = `invest_repay`.`invest_id` AND
                                                          `last_repay`.`period` = `invest_repay`.`period` - 1))
           END * (`loan`.`base_rate` + `loan`.`activity_rate`) * `coupon`.`amount` / 365) AS expected_interest,
     0                                                                                    AS expected_fee,
     floor(CASE
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'INVEST_INTEREST_MONTHLY_REPAY' AND
                `invest_repay`.`status` = 'COMPLETE'
             THEN datediff(`invest_repay`.`actual_repay_date`, `invest`.`created_time`) + 1
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'LOAN_INTEREST_MONTHLY_REPAY' AND
                `invest_repay`.`status` = 'COMPLETE'
             THEN datediff(`invest_repay`.`actual_repay_date`, `loan`.`recheck_time`) + 1
           WHEN `invest_repay`.`period` != 1 AND `invest_repay`.`status` = 'COMPLETE'
             THEN datediff(`invest_repay`.`actual_repay_date`, (SELECT `last_repay`.`actual_repay_date`
                                                                FROM `invest_repay` `last_repay`
                                                                WHERE
                                                                  `last_repay`.`invest_id` = `invest_repay`.`invest_id`
                                                                  AND
                                                                  `last_repay`.`period` = `invest_repay`.`period` - 1))
           ELSE 0
           END * (`loan`.`base_rate` + `loan`.`activity_rate`) * `coupon`.`amount` / 365) AS `actual_interest`,
     0                                                                                    AS actual_fee,
     0                                                                                    AS repay_amount,
     `invest_repay`.`repay_date`                                                          AS `repay_date`,
     `invest_repay`.`actual_repay_date`                                                   AS `actual_repay_date`,
     `invest_repay`.`is_transferred`                                                      AS `is_transferred`,
     `invest_repay`.`status`                                                              AS `status`,
     `invest_repay`.`created_time`                                                        AS `created_time`
   FROM `user_coupon`
     JOIN `coupon` ON `user_coupon`.`coupon_id` = `coupon`.id
     JOIN invest ON user_coupon.invest_id = invest.id
     JOIN `invest_repay` ON `user_coupon`.`invest_id` = `invest_repay`.`invest_id`
     JOIN loan ON user_coupon.loan_id = loan.id
   WHERE `coupon`.`coupon_type` = 'NEWBIE_COUPON' AND `user_coupon`.`status` = 'SUCCESS' AND
         `coupon`.`product_types` != 'EXPERIENCE');

# migrate NEWBIE_COUPON
INSERT INTO coupon_repay (`login_name`,
                          `coupon_id`,
                          `user_coupon_id`,
                          `invest_id`,
                          `period`,
                          `expected_interest`,
                          `expected_fee`,
                          `actual_interest`,
                          `actual_fee`,
                          `repay_amount`,
                          `repay_date`,
                          `actual_repay_date`,
                          `is_transferred`,
                          `status`,
                          `created_time`)

  (SELECT
     `invest`.`login_name`              AS `login_name`,
     `coupon`.`id`                      AS `coupon_id`,
     `user_coupon`.`id`                 AS `user_coupon_id`,
     `invest`.`id`                      AS `invest_id`,
     `invest_repay`.`period`            AS `period`,
     floor(CASE
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'INVEST_INTEREST_MONTHLY_REPAY'
             THEN datediff(`invest_repay`.`repay_date`, `invest`.`created_time`) + 1
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'LOAN_INTEREST_MONTHLY_REPAY'
             THEN datediff(`invest_repay`.`repay_date`, `loan`.`recheck_time`) + 1
           END * ((`loan`.`base_rate` + `loan`.`activity_rate`) * `coupon`.`birthday_benefit`) * `invest`.`amount` /
           365)                         AS expected_interest,
     0                                  AS expected_fee,
     floor(CASE
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'INVEST_INTEREST_MONTHLY_REPAY' AND
                `invest_repay`.`status` = 'COMPLETE' AND `invest_repay`.`is_transferred` IS FALSE
             THEN datediff(`invest_repay`.`repay_date`, `invest`.`created_time`) + 1
           WHEN `invest_repay`.`period` = 1 AND `loan`.`type` = 'LOAN_INTEREST_MONTHLY_REPAY' AND
                `invest_repay`.`status` = 'COMPLETE' AND `invest_repay`.`is_transferred` IS FALSE
             THEN datediff(`invest_repay`.`repay_date`, `loan`.`recheck_time`) + 1
           ELSE 0
           END * ((`loan`.`base_rate` + `loan`.`activity_rate`) * `coupon`.`birthday_benefit`) * `invest`.`amount` /
           365)                         AS `actual_interest`,
     0                                  AS actual_fee,
     0                                  AS repay_amount,
     `invest_repay`.`repay_date`        AS `repay_date`,
     `invest_repay`.`actual_repay_date` AS `actual_repay_date`,
     `invest_repay`.`is_transferred`    AS `is_transferred`,
     `invest_repay`.`status`            AS `status`,
     `invest_repay`.`created_time`      AS `created_time`
   FROM `user_coupon`
     JOIN `coupon` ON `user_coupon`.`coupon_id` = `coupon`.id
     JOIN invest ON user_coupon.invest_id = invest.id
     JOIN `invest_repay` ON `user_coupon`.`invest_id` = `invest_repay`.`invest_id`
     JOIN loan ON user_coupon.loan_id = loan.id
   WHERE
     `coupon`.`coupon_type` = 'BIRTHDAY_COUPON' AND `user_coupon`.`status` = 'SUCCESS' AND `invest_repay`.`period` = 1);

UPDATE `coupon_repay`
SET `coupon_repay`.`expected_fee` = floor(`coupon_repay`.`expected_interest` * (SELECT `invest`.`invest_fee_rate`
                                                                                FROM `invest`
                                                                                WHERE `invest`.`id` =
                                                                                      `coupon_repay`.`invest_id`)),
  `coupon_repay`.`actual_fee`     = floor(`coupon_repay`.`actual_interest` * (SELECT `invest`.`invest_fee_rate`
                                                                              FROM `invest`
                                                                              WHERE `invest`.`id` =
                                                                                    `coupon_repay`.`invest_id`));

UPDATE `coupon_repay`
SET `coupon_repay`.`repay_amount` = `coupon_repay`.`actual_interest` - `coupon_repay`.`actual_fee`;

COMMIT;