BEGIN;
-- update table account
UPDATE `aa`.`account`
SET membership_point =
CASE
WHEN (SELECT sum(floor(amount / 100))
      FROM invest
      WHERE account.login_name = invest.login_name AND invest.status = 'SUCCESS' AND invest.transfer_invest_id IS NULL)
     > 0
  THEN (SELECT sum(floor(amount / 100))
        FROM invest
        WHERE
          account.login_name = invest.login_name AND invest.status = 'SUCCESS' AND invest.transfer_invest_id IS NULL)
ELSE
  0
END;

-- update user_membership
INSERT INTO `aa`.`user_membership` (login_name, membership_id, expired_time, created_time, type)
  SELECT
    `user`.login_name,
    (
      CASE
      WHEN (SELECT `account`.membership_point
            FROM `aa`.`account`
            WHERE `account`.login_name = `user`.`login_name`)
           >= (SELECT `membership`.experience
               FROM `aa`.`membership`
               WHERE `membership`.level = 1)
           AND (SELECT `account`.membership_point
                FROM `aa`.`account`
                WHERE `account`.login_name = `user`.`login_name`)
               < (SELECT `membership`.experience
                  FROM `aa`.`membership`
                  WHERE `membership`.level = 2)
        THEN
          (SELECT `membership`.id
           FROM `aa`.`membership`
           WHERE `membership`.level = 1)
      WHEN (SELECT `account`.membership_point
            FROM `aa`.`account`
            WHERE `account`.login_name = `user`.`login_name`)
           >= (SELECT `membership`.experience
               FROM `aa`.`membership`
               WHERE `membership`.level = 2)
           AND (SELECT `account`.membership_point
                FROM `aa`.`account`
                WHERE `account`.login_name = `user`.`login_name`)
               < (SELECT `membership`.experience
                  FROM `aa`.`membership`
                  WHERE `membership`.level = 3)
        THEN
          (SELECT `membership`.id
           FROM `aa`.`membership`
           WHERE `membership`.level = 2)
      WHEN (SELECT `account`.membership_point
            FROM `aa`.`account`
            WHERE `account`.login_name = `user`.`login_name`)
           >= (SELECT `membership`.experience
               FROM `aa`.`membership`
               WHERE `membership`.level = 3)
           AND (SELECT `account`.membership_point
                FROM `aa`.`account`
                WHERE `account`.login_name = `user`.`login_name`)
               < (SELECT `membership`.experience
                  FROM `aa`.`membership`
                  WHERE `membership`.level = 4)
        THEN
          (SELECT `membership`.id
           FROM `aa`.`membership`
           WHERE `membership`.level = 3)
      WHEN (SELECT `account`.membership_point
            FROM `aa`.`account`
            WHERE `account`.login_name = `user`.`login_name`)
           >= (SELECT `membership`.experience
               FROM `aa`.`membership`
               WHERE `membership`.level = 4)
           AND (SELECT `account`.membership_point
                FROM `aa`.`account`
                WHERE `account`.login_name = `user`.`login_name`)
               < (SELECT `membership`.experience
                  FROM `aa`.`membership`
                  WHERE `membership`.level = 5)
        THEN
          (SELECT `membership`.id
           FROM `aa`.`membership`
           WHERE `membership`.level = 4)
      WHEN (SELECT `account`.membership_point
            FROM `aa`.`account`
            WHERE `account`.login_name = `user`.`login_name`)
           >= (SELECT `membership`.experience
               FROM `aa`.`membership`
               WHERE `membership`.level = 5)
        THEN
          (SELECT `membership`.id
           FROM `aa`.`membership`
           WHERE `membership`.level = 5)
      ELSE
        (SELECT `membership`.id
         FROM `aa`.`membership`
         WHERE `membership`.level = 0)
      END),
    '9999-12-31 23:59:59',
    now(),
    'UPGRADE'
  FROM `aa`.`user`;

-- update membership_experience_bill
-- insert detail info
INSERT INTO `aa`.`membership_experience_bill` (login_name, experience, created_time, description, total_experience)
  SELECT
    `invest`.login_name,
    floor(`invest`.amount / 100),
    `invest`.trading_time,
    concat('投资', `invest`.loan_id, '项目',
           round(`invest`.amount / 100, 2), '元'),
    (SELECT ifnull(sum(floor(`temp`.amount / 100)), 0)
     FROM invest temp
     WHERE temp.id <= invest.id AND invest.login_name = temp.login_name AND temp.status = 'SUCCESS' AND
           temp.transfer_invest_id IS NULL)
  FROM `aa`.`invest`
  WHERE `invest`.transfer_invest_id IS NULL AND `invest`.status = 'SUCCESS';

-- invest invest_fee_rate
UPDATE `aa`.`invest`
SET `invest`.invest_fee_rate =
(SELECT `loan`.invest_fee_rate
 FROM `aa`.`loan`
 WHERE `loan`.id = `invest`.loan_id);

COMMIT;