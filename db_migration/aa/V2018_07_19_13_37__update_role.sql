begin;
update `user_role` set `role` = 'UMP_INVESTOR' where `role` = 'INVESTOR';
update `user_role` set `role` = 'UMP_LOANER' where `role` = 'LOANER';

update `invest_referrer_reward` set `referrer_role` = 'UMP_INVESTOR' where `referrer_role` = 'INVESTOR';

commit;