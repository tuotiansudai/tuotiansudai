ALTER TABLE jpush_alert ADD COLUMN push_user_type VARCHAR(28) AFTER push_source;
ALTER TABLE jpush_alert ADD COLUMN expect_push_time DATETIME AFTER jump_to_link;