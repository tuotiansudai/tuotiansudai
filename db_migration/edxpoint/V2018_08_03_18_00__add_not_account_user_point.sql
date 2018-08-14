INSERT ignore into user_point(login_name, point, updated_time)
SELECT
  aa.user.login_name,
  '0'   AS 'point',
  now() AS 'updated_time'
FROM aa.user
WHERE NOT exists(SELECT 1
                 FROM user_point
                 WHERE login_name = aa.user.login_name);