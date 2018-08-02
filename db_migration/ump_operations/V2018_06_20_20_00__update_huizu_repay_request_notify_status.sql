begin;

update hz_nopwd_repay_request set status = 'SUCCESS' where id = 100863;
update hz_repay_notify_request set status = 'DONE' where id= 101127;

COMMIT;