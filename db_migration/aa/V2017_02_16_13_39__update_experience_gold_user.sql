begin;
--更新未使用体验金
update user set experience_gold=688800 where login_name in (
  select login_name from aa.user_coupon
  where coupon_id in (select id from aa.coupon where coupon_type='NEWBIE_COUPON' and product_types='EXPERIENCE')
  and end_time > now()
);
commit;