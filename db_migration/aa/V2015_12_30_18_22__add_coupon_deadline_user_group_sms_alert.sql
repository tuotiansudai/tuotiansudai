
alter table coupon add deadline INT UNSIGNED ;

alter table coupon add user_group varchar(32);

alter table coupon add sms_alert TINYINT(1);

