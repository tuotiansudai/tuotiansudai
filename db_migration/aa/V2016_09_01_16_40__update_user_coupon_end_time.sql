BEGIN ;
update coupon set deadline = 30 where id in (305,306,307,308);
update user_coupon set end_time = '2016-10-01 23:59:59' where coupon_id in (305,306,307,308) and end_time = '2200-12-30 23:59:59';
COMMIT;