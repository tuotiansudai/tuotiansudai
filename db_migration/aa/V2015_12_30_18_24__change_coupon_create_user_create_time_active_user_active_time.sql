ALTER TABLE coupon CHANGE `create_time` `created_time` DATETIME NOT NULL;

alter table coupon drop foreign key FK_CREATE_USER_REF_USER_LOGIN_NAME;

ALTER TABLE coupon CHANGE `create_user` `created_by` VARCHAR(25) NOT NULL;

alter table coupon add constraint FK_CREATED_BY_REF_USER_LOGIN_NAME foreign key (created_by) references user(login_name);



ALTER TABLE coupon CHANGE `active_time` `actived_time` DATETIME NOT NULL;

alter table coupon drop foreign key FK_ACTIVATE_USER_REF_USER_LOGIN_NAME;

ALTER TABLE coupon CHANGE `active_user` `actived_by` VARCHAR(25) NOT NULL;

alter table coupon add constraint FK_ACTIVED_BY_REF_USER_LOGIN_NAME foreign key (actived_by) references user(login_name);

