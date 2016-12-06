ALTER TABLE loan_details ADD COLUMN `push_message` VARCHAR(100);

ALTER TABLE message ADD COLUMN `push_id` BIGINT UNSIGNED DEFAULT NULL
AFTER `read_count`;

ALTER TABLE `message` CHANGE `user_groups` `user_group` VARCHAR(100);

ALTER TABLE jpush_alert DROP COLUMN `name`;
ALTER TABLE jpush_alert DROP COLUMN `push_districts`;
ALTER TABLE jpush_alert DROP COLUMN `push_user_type`;
ALTER TABLE jpush_alert DROP COLUMN `jump_to`;
ALTER TABLE jpush_alert DROP COLUMN `jump_to_link`;
ALTER TABLE jpush_alert DROP COLUMN `expect_push_time`;
ALTER TABLE jpush_alert DROP COLUMN `auditor`;
ALTER TABLE jpush_alert DROP COLUMN `is_automatic`;
ALTER TABLE jpush_alert DROP COLUMN `ios_target_num`;
ALTER TABLE jpush_alert DROP COLUMN `ios_arrive_num`;
ALTER TABLE jpush_alert DROP COLUMN `ios_open_num`;
ALTER TABLE jpush_alert DROP COLUMN `android_target_num`;
ALTER TABLE jpush_alert DROP COLUMN `android_arrive_num`;
ALTER TABLE jpush_alert DROP COLUMN `android_open_num`;
ALTER TABLE jpush_alert DROP COLUMN `status`;