BEGIN;

INSERT INTO point_bill (
  order_id,
  login_name,
  POINT,
  business_type,
  note,
  created_time
)
  SELECT
    1,
    t.`login_name`,
    50,
    'TASK',
    'REGISTER',
    t.`register_time`
  FROM
    account t;

INSERT INTO point_bill (
  order_id,
  login_name,
  POINT,
  business_type,
  note,
  created_time
)
  SELECT
    2,
    u.`login_name`,
    50,
    'TASK',
    'BIND_EMAIL',
    IFNULL(
        u.`last_modified_time`,
        u.`register_time`
    )
  FROM
    `user` u
  WHERE u.`email` IS NOT NULL
        AND u.`email` != '';

INSERT INTO point_bill (
  order_id,
  login_name,
  POINT,
  business_type,
  note,
  created_time
)
  SELECT
    3,
    b.`login_name`,
    50,
    'TASK',
    'BIND_BANK_CARD',
    b.`created_time`
  FROM
    bank_card b
  WHERE b.`status` = 'PASSED';

INSERT INTO point_bill (
  order_id,
  login_name,
  POINT,
  business_type,
  note,
  created_time
)
  SELECT
    4,
    r.`login_name`,
    100,
    'TASK',
    'FIRST_RECHARGE',
    MIN(r.`created_time`)
  FROM
    recharge r
  WHERE r.`status` = 'SUCCESS'
  GROUP BY r.`login_name`;

INSERT INTO point_bill (
  order_id,
  login_name,
  POINT,
  business_type,
  note,
  created_time
)
  SELECT
    5,
    i.`login_name`,
    200,
    'TASK',
    'FIRST_INVEST',
    MIN(i.`created_time`)
  FROM
    invest i
  WHERE i.`status` = 'SUCCESS'
  GROUP BY i.`login_name`;

INSERT INTO point_bill (
  order_id,
  login_name,
  POINT,
  business_type,
  note,
  created_time
)
  SELECT
    6,
    it.`login_name`,
    500,
    'TASK',
    'SUM_INVEST_10000',
    MAX(it.`created_time`)
  FROM
    invest it
  WHERE it.`status` = 'SUCCESS'
  GROUP BY it.`login_name`
  HAVING SUM(it.`amount`) >= 1000000;

INSERT INTO point_bill (
  order_id,
  login_name,
  POINT,
  business_type,
  note,
  created_time
)
  SELECT
    m.`id`,
    m.`login_name`,
    FLOOR(m.`amount` / 100),
    'INVEST',
    CONCAT('投资项目：', l.name),
    m.`created_time`
  FROM
    invest m
    JOIN loan l
      ON m.`loan_id` = l.`id`
  WHERE m.`status` = 'SUCCESS';

UPDATE
  account a
  JOIN
    (SELECT
      p.`login_name`,
      SUM(p.`point`) AS `point`
    FROM
      point_bill p
    GROUP BY p.`login_name`) TEMP
    ON a.`login_name` = TEMP.`login_name` SET a.`point` = TEMP.`point`;

COMMIT;