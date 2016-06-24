ALTER TABLE point_task ADD multiple BOOLEAN DEFAULT FALSE
AFTER `point`;

ALTER TABLE point_task ADD active BOOLEAN DEFAULT TRUE
AFTER `multiple`;


ALTER TABLE user_point_task ADD COLUMN task_level BIGINT UNSIGNED NOT NULL DEFAULT 1
AFTER `point_task_id`;

ALTER TABLE user_point_task ADD point BIGINT UNSIGNED NOT NULL DEFAULT 0
AFTER `task_level`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`login_name`, `point_task_id`, `task_level`);

