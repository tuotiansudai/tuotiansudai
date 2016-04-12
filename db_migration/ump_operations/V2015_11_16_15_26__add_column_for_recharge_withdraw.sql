alter table `ump_operations`.`recharge_notify_request` add column `com_amt_type` VARCHAR(6) after `com_amt`;
alter table `ump_operations`.`withdraw_notify_request` add column `com_amt` VARCHAR(26) after `amount`;
alter table `ump_operations`.`withdraw_notify_request` add column `com_amt_type` VARCHAR(6) after `com_amt`;