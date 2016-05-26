ALTER TABLE point_task ADD active BOOLEAN DEFAULT 0;
ALTER TABLE point_task ADD multiple BOOLEAN DEFAULT 0;
ALTER TABLE user_point_task ADD point BIGINT UNSIGNED DEFAULT 0;
ALTER TABLE user_point_task ADD COLUMN task_level BIGINT UNSIGNED NOT NULL AFTER point,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`login_name`, `point_task_id`, `task_level`);

