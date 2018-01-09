ALTER TABLE `user_point`
  ADD COLUMN `sudai_point` BIGINT UNSIGNED NOT NULL DEFAULT 0
  AFTER `point`;

ALTER TABLE `user_point`
  ADD COLUMN `channel_point` BIGINT UNSIGNED NOT NULL DEFAULT 0
  AFTER `sudai_point`;

ALTER TABLE `user_point`
  ADD COLUMN `channel` VARCHAR(50) NULL
  AFTER `channel_point`;

ALTER TABLE `user_point`
  ADD INDEX `USER_POINT_CHANNEL`(`channel`);

UPDATE user_point
SET sudai_point = point
WHERE 1 = 1;

ALTER TABLE `point_bill`
  ADD COLUMN `sudai_point` BIGINT NOT NULL DEFAULT 0
  AFTER `point`;

ALTER TABLE `point_bill`
  ADD COLUMN `channel_point` BIGINT NOT NULL DEFAULT 0
  AFTER `sudai_point`;

UPDATE point_bill
SET sudai_point = point
WHERE 1 = 1;
