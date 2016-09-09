BEGIN;
USE aa;
-- delete origin table
DROP TABLE point_bill;
DROP TABLE user_point_task;
-- replace origin table with temp table
ALTER TABLE `aa`.`point_bill_temp` RENAME TO `aa`.`point_bill`;
ALTER TABLE `aa`.`user_point_task_temp` RENAME TO `aa`.`user_point_task`;
COMMIT;