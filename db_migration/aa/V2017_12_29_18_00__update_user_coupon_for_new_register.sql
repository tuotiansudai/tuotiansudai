BEGIN;
update coupon set end_time='2020-12-31 23:59:59' where id in (383,384,385,386,387,389,390,391);
update coupon set coupon_source='首投礼奖励' where id = 391;
COMMIT;
