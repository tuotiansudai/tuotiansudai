BEGIN;
INSERT INTO `user` (
  `login_name`,
  `password`,
  `email`,
  `address`,
  `mobile_number`,
  `last_login_time`,
  `register_time`,
  `last_modified_time`,
  `last_modified_user`,
  `forbidden_time`,
  `avatar`,
  `referrer`,
  `status`,
  `salt`
)
SELECT
  t.`username` AS login_name,
  t.`password`,
  t.`email`,
  t.`current_address` AS address,
  t.`mobile_number`,
  t.`last_login_time`,
  t.`register_time`,
  NULL AS last_modified_time,
  NULL AS last_modified_user,
  t.`disable_time` AS forbidden_time,
  t.`photo` AS avatar,
  NULL AS referrer,
  CASE
    t.`status`
    WHEN '1'
    THEN 'active'
    ELSE 'inactive'
  END AS STATUS,
  REPLACE(UUID(), '-', '') AS salt
FROM
  `user_history` t
WHERE t.`mobile_number` != ''
  AND t.`email` != '' ;
COMMIT;