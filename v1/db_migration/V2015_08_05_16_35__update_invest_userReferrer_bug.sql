BEGIN ;
UPDATE
  invest_userReferrer ur
  JOIN
    (SELECT
      temp1.deadline,
      temp1.repay_time_unit,
      temp1.`investId` AS investId,
      temp1.`money`,
      temp1.`user_id`,
      n.`referrer_id`,
      n.`level`,
      temp1.repayWay,
      IF(
        n.`level` = 1,
        ROUND(
          temp1.`money` * 0.01 * temp1.deadline / temp1.repayWay,
          2
        ),
        ROUND(
          temp1.`money` * 0.002 * temp1.deadline / temp1.repayWay,
          2
        )
      ) AS bonus
    FROM
      (SELECT
        temp.deadline,
        temp.repay_time_unit,
        i.`id` AS investId,
        i.`money`,
        i.`user_id`,
        IF(
          temp.repay_time_unit = 'month',
          12,
          365
        ) AS repayWay
      FROM
        (SELECT
          l.id AS loanId,
          l.`deadline`,
          p.`repay_time_unit`
        FROM
          loan l
          JOIN loan_type p
            ON l.`type` = p.`id`) temp
        JOIN invest i
          ON temp.loanId = i.`loan_id`
          AND i.`status` IN ('complete', 'repaying')) temp1
      JOIN referrer_relation n
        ON n.`user_id` = temp1.user_id) correct
    ON ur.`invest_id` = correct.`investId`
    AND ur.`referrer_id` = correct.`referrer_id`
    AND ur.`role_name` = 'ROLE_MERCHANDISER' SET ur.`bonus` = correct.bonus ;
COMMIT ;
