ALTER TABLE `aa`.`loan` ADD COLUMN `loaner_user_name` VARCHAR(50) NULL
AFTER loaner_login_name;
ALTER TABLE `aa`.`loan` ADD COLUMN `loaner_identity_number` VARCHAR(20) NULL
AFTER loaner_user_name;

UPDATE `loan` l INNER JOIN `account` a
    ON l.loaner_login_name = a.login_name
SET l.`loaner_user_name`     = a.`user_name`,
  l.`loaner_identity_number` = a.`identity_number`;

ALTER TABLE aa.loan DROP FOREIGN KEY `FK_LOAN_LOANER_LOGIN_NAME_REF_USER_LOGIN_NAME`;
ALTER TABLE aa.loan DROP INDEX `FK_LOAN_LOANER_LOGIN_NAME_REF_USER_LOGIN_NAME`;
ALTER TABLE aa.loan MODIFY COLUMN loaner_user_name VARCHAR(50) NOT NULL;
ALTER TABLE aa.loan MODIFY COLUMN loaner_identity_number VARCHAR(20) NOT NULL;
