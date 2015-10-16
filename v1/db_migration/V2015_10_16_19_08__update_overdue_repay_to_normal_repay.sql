begin;

update `user_bill` set `type_info`='normal_repay' where `type_info`='overdue_repay';

commit;