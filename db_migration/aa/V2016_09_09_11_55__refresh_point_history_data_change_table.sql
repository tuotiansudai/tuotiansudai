BEGIN;
USE aa;
-- delete origin table
ALTER TABLE `aa`.`point_bill` RENAME TO `aa`.`point_bill_backup`;
ALTER TABLE `aa`.`user_point_task` RENAME TO `aa`.`user_point_task_backup`;
-- replace origin table with temp table
ALTER TABLE `aa`.`point_bill_temp` RENAME TO `aa`.`point_bill`;
ALTER TABLE `aa`.`user_point_task_temp` RENAME TO `aa`.`user_point_task`;
COMMIT;