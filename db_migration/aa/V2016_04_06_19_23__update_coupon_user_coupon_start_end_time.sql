alter table coupon drop column deadline;
alter table coupon drop column invest_upper_limit;

BEGIN ;

delete from coupon where user_group = 'EXCHANGER';

COMMIT ;