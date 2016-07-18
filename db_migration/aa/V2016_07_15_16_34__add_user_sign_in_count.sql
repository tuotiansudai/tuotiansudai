BEGIN ;
alter table user add column sign_in_count BIGINT(20) default 0;
COMMIT ;