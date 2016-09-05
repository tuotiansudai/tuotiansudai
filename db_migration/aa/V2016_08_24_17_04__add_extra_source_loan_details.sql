BEGIN ;
ALTER TABLE `aa`.`loan_details`
ADD COLUMN `extra_source` VARCHAR(32) NULL;
COMMIT ;