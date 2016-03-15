ALTER TABLE jpush_alert ADD COLUMN auditor VARCHAR(25) AFTER updated_by;
ALTER TABLE jpush_alert ADD COLUMN ios_target_num INT(16);
ALTER TABLE jpush_alert ADD COLUMN ios_arrive_num INT(16);
ALTER TABLE jpush_alert ADD COLUMN ios_open_num INT(16);
ALTER TABLE jpush_alert ADD COLUMN android_target_num INT(16);
ALTER TABLE jpush_alert ADD COLUMN android_arrive_num INT(16);
ALTER TABLE jpush_alert ADD COLUMN android_open_num INT(16);
