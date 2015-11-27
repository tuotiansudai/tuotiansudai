alter table `aa`.`loan` add column `loaner_user_name` VARCHAR(50) NULL after loaner_login_name;
alter table `aa`.`loan` add column `loaner_identity_number` VARCHAR(20) NULL after loaner_user_name;

update `loan` l inner join `account` a
    on l.loaner_login_name = a.login_name
set l.`loaner_user_name` = a.`user_name`,
    l.`loaner_identity_number` = a.`identity_number`;

alter table aa.loan drop FOREIGN KEY `FK_LOAN_LOANER_LOGIN_NAME_REF_USER_LOGIN_NAME`;
alter table aa.loan drop index `FK_LOAN_LOANER_LOGIN_NAME_REF_USER_LOGIN_NAME`;
alter table aa.loan drop column loaner_login_name;
alter table aa.loan modify column loaner_user_name VARCHAR(50) NOT NULL;
alter table aa.loan modify column loaner_identity_number VARCHAR(20) NOT NULL;
