begin;
alter table invest_extra_rate add column status varchar(16) default 'REPAYING' NOT NULL;
COMMIT ;