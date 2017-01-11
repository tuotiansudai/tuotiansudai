begin;
insert into membership_privilege(login_name,privilege,start_time,end_time,created_time)
 select login_name,
       'SERVICE_FEE',
        created_time as 'start_time',
      	DATE_ADD(DATE_ADD(str_to_date(DATE_FORMAT(created_time,'%Y-%m-%d'),'%Y-%m-%d %H:%i:%s'),INTERVAL duration+1 DAY),INTERVAL -1 SECOND) as 'end_time',
        created_time
  from membership_purchase
 where status= 'SUCCESS' ;
COMMIT ;