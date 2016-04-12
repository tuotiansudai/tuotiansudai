ALTER TABLE `aa`.`user_bill` DROP FOREIGN KEY FK_USER_BILL_OPERATOR_LOGIN_NAME_REF_USER_LOGIN_NAME;


ALTER TABLE `aa`.`user_bill` CHANGE `operation_login_name` `operator_login_name` VARCHAR(25)
AFTER `business_type`;

ALTER TABLE `aa`.`user_bill` ADD CONSTRAINT FK_USER_BILL_OPERATOR_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`operator_login_name`) REFERENCES `aa`.`user` (`login_name`);

ALTER TABLE `aa`.`user_bill` CHANGE `intervention_reason` `intervention_reason` TEXT
AFTER `operator_login_name`;