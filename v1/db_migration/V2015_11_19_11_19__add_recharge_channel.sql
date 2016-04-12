BEGIN;
alter table recharge add column channel varchar(32);

commit;