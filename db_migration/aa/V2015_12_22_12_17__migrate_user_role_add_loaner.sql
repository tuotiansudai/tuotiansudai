BEGIN ;

INSERT INTO user_role
SELECT
  t.`agent_login_name`,
  'LOANER',
  NOW()
FROM
  loan t
WHERE NOT EXISTS
  (SELECT
    1
  FROM
    user_role u
  WHERE t.`agent_login_name` = u.`login_name`
    AND u.`role` = 'LOANER')
GROUP BY `agent_login_name`;

COMMIT ;