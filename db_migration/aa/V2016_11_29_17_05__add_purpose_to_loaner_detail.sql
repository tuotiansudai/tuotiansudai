ALTER TABLE `loaner_details`
ADD COLUMN `purpose` VARCHAR(10) NULL DEFAULT NULL
AFTER `employment_status`;