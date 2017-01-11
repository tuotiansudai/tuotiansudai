begin;
insert into membership_privilege_purchase(login_name,mobile,user_name,privilege,privilege_price_type,amount,source,status,created_time)
  select login_name,
       mobile,
       user_name,
       'SERVICE_FEE',
       case duration when 30 then '_30' when 180 then '_180' when 360 then '_360' end as 'privilege_price_type',
       amount,
       source,
       status,
       created_time
  from membership_purchase
 where status= 'SUCCESS' ;
COMMIT ;