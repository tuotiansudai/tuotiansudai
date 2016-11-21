begin;
alter table prepare_user add column referrer_login_name varchar(25);
alter table  prepare_user add constraint FK_PREPARE_USER_LOGIN_NAME_REF_USER_LOGIN_NAME foreign key(referrer_login_name)
references user(login_name);

alter table booking_loan add column login_name varchar(25);
alter table  booking_loan add constraint FK_BOOKING_LOGIN_NAME_REF_USER_LOGIN_NAME foreign key(login_name)
references user(login_name);
commit;