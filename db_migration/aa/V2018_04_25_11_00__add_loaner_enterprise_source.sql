ALTER TABLE `aa`.`loaner_enterprise_details`
ADD COLUMN `source` VARCHAR(100) NULL;

ALTER TABLE `aa`.`loaner_enterprise_info`
ADD COLUMN `source` VARCHAR(100) NULL AFTER `purpose`;
