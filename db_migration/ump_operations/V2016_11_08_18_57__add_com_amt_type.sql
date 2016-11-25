ALTER TABLE `ump_operations`.`mer_recharge_person_request` ADD COLUMN `com_amt_type` VARCHAR(2)
AFTER `gate_id`;

ALTER TABLE `ump_operations`.`mer_recharge_request` ADD COLUMN `com_amt_type` VARCHAR(2)
AFTER `gate_id`;

ALTER TABLE `ump_operations`.`cust_withdrawals_request` ADD COLUMN `com_amt_type` VARCHAR(2)
AFTER `amount`;

ALTER TABLE `ump_operations`.`withdraw_apply_notify_request` ADD COLUMN `com_amt` VARCHAR(26)
AFTER `amount`;

ALTER TABLE `ump_operations`.`withdraw_apply_notify_request` ADD COLUMN `com_amt_type` VARCHAR(6)
AFTER `com_amt`;