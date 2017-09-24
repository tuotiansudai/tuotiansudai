begin;
alter table loan_details add column disable_coupon tinyint(1) NOT NULL DEFAULT '0';
COMMIT;
