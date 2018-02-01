BEGIN;
update coupon set user_group = 'WINNER' where id in (481, 482);
COMMIT;