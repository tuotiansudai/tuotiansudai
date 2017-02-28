begin;
update loan set name='拓天体验金项目', loan_amount = 1068, min_invest_amount = 5000, base_rate = 0.13 where id = 1;

update user set experience_balance = 688800 where login_name in (
  select login_name from aa.user_coupon
  where coupon_id in (select id from aa.coupon where coupon_type='NEWBIE_COUPON' and product_types='EXPERIENCE')
  and end_time > now()
);

update coupon set deleted = 1 where amount = 688800 and coupon_type = 'EXPERIENCE';
commit;