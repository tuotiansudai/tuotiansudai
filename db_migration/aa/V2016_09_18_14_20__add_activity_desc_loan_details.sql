ALTER TABLE `aa`.`loan_details`
DROP COLUMN `activity`;

ALTER TABLE `aa`.`loan_details`
ADD COLUMN `activity_desc` VARCHAR(10) DEFAULT "";