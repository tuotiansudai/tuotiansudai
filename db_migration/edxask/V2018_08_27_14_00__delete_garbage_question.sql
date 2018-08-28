delete from question where `created_time` BETWEEN '2018-08-25 00:00:00' and now() and login_name in
(select temp.login_name from (select login_name from question where `created_time` BETWEEN '2018-08-25 00:00:00' and now() and status in('UNAPPROVED', 'REJECTED') group by login_name having count(login_name) > 10) temp)