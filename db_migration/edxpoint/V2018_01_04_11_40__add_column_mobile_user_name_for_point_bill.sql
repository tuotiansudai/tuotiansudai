ALTER TABLE `point_bill`
  ADD COLUMN `mobile` VARCHAR(18) NOT NULL
  AFTER `login_name`;

ALTER TABLE `point_bill`
  ADD COLUMN `user_name` VARCHAR(50) NOT NULL
  AFTER `login_name`;


