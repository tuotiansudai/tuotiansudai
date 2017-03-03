begin;
update coupon set multiple = 1, deadline = 30 where id >= 392 and id <= 399;
commit;
