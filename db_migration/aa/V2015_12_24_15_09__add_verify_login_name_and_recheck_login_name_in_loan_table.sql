ALTER TABLE `aa`.`loan` ADD COLUMN created_login_name VARCHAR(25) NULL AFTER created_time;
ALTER TABLE `aa`.`loan` ADD COLUMN verify_login_name VARCHAR(25) NULL AFTER verify_time;
ALTER TABLE `aa`.`loan` ADD COLUMN recheck_login_name VARCHAR(25) NULL AFTER recheck_time;