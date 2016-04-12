ALTER TABLE `aa`.`user_bill` ADD `operation_login_name` VARCHAR(25) AFTER `operation_type`;
ALTER TABLE `aa`.`user_bill` ADD `intervention_reason` TEXT AFTER `operation_login_name`;
ALTER TABLE `aa`.`user_bill` ADD CONSTRAINT FK_USER_BILL_OPERATOR_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`operation_login_name`) REFERENCES `aa`.`user` (`login_name`);