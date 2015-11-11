BEGIN;
alter table user add column channel varchar(32);

alter table invest add column channel varchar(32);

commit;