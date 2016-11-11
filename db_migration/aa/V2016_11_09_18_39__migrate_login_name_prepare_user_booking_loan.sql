begin;
update prepare_user a set referrer_login_name = (select login_name from user u where u.mobile=a.referrer_mobile);

update booking_loan a set login_name = (select login_name from user u where u.mobile=a.mobile);
commit;