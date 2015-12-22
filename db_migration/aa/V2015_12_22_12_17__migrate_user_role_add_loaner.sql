BEGIN ;

INSERT INTO user_role
SELECT
  t.`login_name`,
  'LOANER',
  NOW()
FROM
  user_role t
WHERE t.role = 'AGENT'
  AND NOT EXISTS
  (SELECT
    1
  FROM
    user_role u
  WHERE t.`login_name` = u.`login_name`
    AND u.`role` = 'LOANER');

COMMIT ;