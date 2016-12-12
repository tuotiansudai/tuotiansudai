ALTER TABLE `edxactivity`.`not_work`
CHANGE COLUMN `mobile` `mobile` VARCHAR(11) NULL ;


ALTER TABLE `edxactivity`.`not_work`
ADD COLUMN `activity_category` VARCHAR(50) NULL DEFAULT 'NO_WORK_ACTIVITY' AFTER `send_coupon`;




