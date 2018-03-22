ALTER TABLE `user`
  DROP COLUMN `sign_in_count_bak`,
  DROP COLUMN `experience_balance_bak`;

ALTER TABLE `account`
  DROP COLUMN `_point`,
  DROP COLUMN `_user_name`,
  DROP COLUMN `_identity_number`;

