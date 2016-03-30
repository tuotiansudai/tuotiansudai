begin;
alter table transfer_application add column transfer_interest_days INT not null AFTER `transfer_amount`;
COMMIT ;