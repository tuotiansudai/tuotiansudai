BEGIN;

INSERT INTO user_point_task
  select distinct rr.referrer_login_name,
  9,
  u.register_time,
  200,
  0
  from
  user u join referrer_relation rr on u.referrer = rr.referrer_login_name;

INSERT INTO user_point_task
  select rr.referrer_login_name,
  10,
  now(),
  CASE floor(i.amount/100000)
  WHEN 0 THEN 1000
  ELSE floor(i.amount/100000)*1000 END,
  (select ifnull(max(task_level),0) + 1 from user_point_task upt
  where rr.referrer_login_name = upt.login_name and point_task_id =10 )
  from referrer_relation rr join invest i on rr.login_name = i.login_name
  where i.status='SUCCESS'
  and i.amount > 100000;

INSERT INTO user_point_task
  select rr.referrer_login_name,
  11,
  now(),
  5000,
  rr.level
  from referrer_relation rr where rr.login_name in(
  select login_name from invest i
  where i.status='SUCCESS'
  group by i.login_name);

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
  and i.`status` = 'SUCCESS'
  and l.`duration` = 180
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
  and i.`login_name` = a.`login_name`
  and i.`status` = 'SUCCESS'
  and a.`no_password_invest` is true
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
  and i.`login_name` = a.`login_name`
  and i.`status` = 'SUCCESS'
  and a.`auto_invest` is true
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
  and i.`status` = 'SUCCESS'
  and l.`duration` = 360
  GROUP BY i.`login_name`;

COMMIT;