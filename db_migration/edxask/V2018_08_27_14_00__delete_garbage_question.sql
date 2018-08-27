delete from question WHERE `created_time` BETWEEN '2018-08-25 00:00:00' and now() and status in ('UNAPPROVED', 'REJECTED');
