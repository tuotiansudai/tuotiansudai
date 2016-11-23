ALTER TABLE `edxactivity`.`activity`
ADD COLUMN `jump_to_link` VARCHAR(50) NOT NULL AFTER `app_activity_url`;