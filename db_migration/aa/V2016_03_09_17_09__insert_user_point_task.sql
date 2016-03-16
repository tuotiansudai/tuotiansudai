BEGIN ;

INSERT INTO user_point_task
SELECT
  t.`login_name`,
  1,
  min(t.`register_time`) as register_time
FROM
  account t group by t.`login_name`;

INSERT INTO user_point_task
  SELECT
    u.`login_name`,
    2,
    IFNULL(
        u.`last_modified_time`,
        u.`register_time`
    )
  FROM
    `user` u
  WHERE u.`email` IS NOT NULL
        AND u.`email` != '' ;

INSERT INTO user_point_task
  SELECT
    b.`login_name`,
    3,
    b.`created_time`
  FROM
    bank_card b
  WHERE b.`status` = 'PASSED' ;

INSERT INTO user_point_task
  SELECT
    r.`login_name`,
    4,
    MIN(r.`created_time`)
  FROM
    recharge r
  WHERE r.`status` = 'SUCCESS'
  GROUP BY r.`login_name` ;

INSERT INTO user_point_task
  SELECT
    i.`login_name`,
    5,
    MIN(i.`created_time`)
  FROM
    invest i
  WHERE i.`status` = 'SUCCESS'
  GROUP BY i.`login_name` ;

INSERT INTO user_point_task
  SELECT
    it.`login_name`,
    6,
    MAX(it.`created_time`)
  FROM
    invest it
  WHERE it.`status` = 'SUCCESS'
  GROUP BY it.`login_name`
  HAVING SUM(it.`amount`) >= 1000000 ;

COMMIT ;