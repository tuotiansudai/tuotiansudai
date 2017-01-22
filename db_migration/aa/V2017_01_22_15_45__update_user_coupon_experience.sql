BEGIN;
insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   376,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;


insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   377,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;


insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   378,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;

insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   379,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;

insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   380,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;

insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   381,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;


insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   382,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;



insert into user_coupon(login_name,
						coupon_id,
						start_time,
                        end_time,
                        loan_id,
                        used_time,
                        expected_interest,
                        actual_interest,
                        default_interest,
                        expected_fee,
                        actual_fee,
                        created_time,
                        invest_id,
                        status,
                        exchange_code,
                        achievement_loan_id)
select tab.login_name,
	   383,
       tab.start_time,
       tab.end_time,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       tab.created_time,
       null,
       null,
       null,
       '0'
       from  (select login_name,min(start_time) as start_time,max(end_time) as end_time,max(created_time)	as created_time
  from user_coupon
 where coupon_id in(315, 316, 317, 318, 319)
   and now() BETWEEN `start_time`
   and `end_time`
   and(`status`!= 'SUCCESS'
    or `status` IS NULL)
   GROUP BY `login_name`
   order by `login_name`) tab;

COMMIT;