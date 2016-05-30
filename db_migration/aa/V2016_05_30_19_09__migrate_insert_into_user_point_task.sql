BEGIN;

INSERT INTO user_point_task
  SELECT rr.`referrer_login_name`,
  9,
  u.`register_time`,
  200,
  0
  FROM
  USER u JOIN referrer_relation rr ON u.`referrer` = rr.`referrer_login_name`;


INSERT INTO user_point_task
  SELECT rr.referrer_login_name,
  10,
  now(),
  CASE floor(i.`amount`/100000)
  WHEN 0 THEN 1000
  ELSE floor(i.`amount`/100000)*1000 END,
  0
  FROM referrer_relation rr JOIN invest i ON rr.`login_name` = i.`login_name`
  WHERE i.`status` = 'SUCCESS'
  AND i.`amount` >= 100000;


INSERT INTO user_point_task
  SELECT rr.`referrer_login_name`,
  11,
  now(),
  5000,
  rr.`level`
  FROM referrer_relation rr WHERE rr.`login_name` IN(
  SELECT login_name FROM invest i
  WHERE i.`status` = 'SUCCESS'
  GROUP BY i.`login_name`);


INSERT INTO user_point_task
  SELECT
    i.`login_name`,
    12,
    MIN(i.`created_time`),
    1000,
    0
  FROM
    invest i, loan l
  WHERE i.loan_id = l.id
  AND i.`status` = 'SUCCESS'
  AND l.`duration` = 180
  GROUP BY i.`login_name`;

INSERT INTO user_point_task
  SELECT
    i.`login_name`,
    13,
    MIN(i.`created_time`),
    1000,
    0
  FROM
    invest i, loan l, account a
  WHERE i.loan_id = l.id
  AND i.`login_name` = a.`login_name`
  AND i.`status` = 'SUCCESS'
  AND a.`no_password_invest` IS TRUE
  GROUP BY i.`login_name`;

INSERT INTO user_point_task
  SELECT
    i.`login_name`,
    14,
    MIN(i.`created_time`),
    1000,
    0
  FROM
    invest i, loan l, account a
  WHERE i.loan_id = l.id
  AND i.`login_name` = a.`login_name`
  AND i.`status` = 'SUCCESS'
  AND a.`auto_invest` IS TRUE
  GROUP BY i.`login_name`;

INSERT INTO user_point_task
  SELECT
    i.`login_name`,
    15,
    MIN(i.`created_time`),
    1000,
    0
  FROM
    invest i, loan l
  WHERE i.loan_id = l.id
  AND i.`status` = 'SUCCESS'
  AND l.`duration` = 360
  GROUP BY i.`login_name`;


COMMIT;