begin;
delete from user_membership where type = 'PURCHASED';
COMMIT ;