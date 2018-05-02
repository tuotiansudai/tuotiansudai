BEGIN ;
ALTER TABLE `aa`.`loan_details`
ADD COLUMN `estimates` VARCHAR(100) NULL;
COMMIT ;