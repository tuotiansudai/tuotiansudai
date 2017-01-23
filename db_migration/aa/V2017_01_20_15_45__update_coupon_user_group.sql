BEGIN;
update loan set status= 'COMPLETE' where id = 1;

insert into `loan`(`id`,`name`,`agent_login_name`,`loaner_login_name`,`loaner_user_name`,`loaner_identity_number`,`type`,`periods`,`duration`,`description_text`,`description_html`,`pledge_type`,`loan_amount`,`min_invest_amount`,`max_invest_amount`,`invest_increasing_amount`,`activity_type`,`product_type`,`base_rate`,`activity_rate`,`contract_id`,`fundraising_start_time`,`fundraising_end_time`,`raising_complete_time`,`verify_time`,`verify_login_name`,`recheck_time`,`recheck_login_name`,`status`,`first_invest_achievement_id`,`last_invest_achievement_id`,`max_amount_achievement_id`,`show_on_home`,`created_time`,`created_login_name`,`update_time`)
values(2,'新手体验项目',null,'','','','INVEST_INTEREST_LUMP_SUM_REPAY','1','3','','','NONE','68880000','0','0','0','NEWBIE','EXPERIENCE',0.15,'0','0','2017-01-01 00:00:00','9999-12-31 00:00:00',null,'2017-01-01 17:38:54','sidneygao',null,null,'RAISING',null,null,null,'1','2017-01-01 17:38:54','sidneygao','2017-01-01 17:38:54');

INSERT INTO coupon (`amount`,
                    `rate`,
                    `birthday_benefit`,
                    `multiple`,
                    `start_time`,
                    `end_time`,
                    `deadline`,
                    `used_count`,
                    `total_count`,
                    `issued_count`,
                    `active`,
                    `created_by`,
                    `created_time`,
                    `activated_by`,
                    `activated_time`,
                    `updated_by`,
                    `updated_time`,
                    `invest_lower_limit`,
                    `product_types`,
                    `coupon_type`,
                    `user_group`,
                    `coupon_source`)
  SELECT
    688800,
    0,
    0,
    0,
    '2017-01-01',
    '9999-12-31',
    30,
    0,
    1000000,
    0,
    TRUE,
    'sidneygao',
    now(),
    'sidneygao',
    now(),
    'sidneygao',
    now(),
    0,
    'EXPERIENCE',
    'NEWBIE_COUPON',
    'NEW_REGISTERED_USER',
    '拓天速贷平台赠送'
  FROM dual
  WHERE EXISTS(SELECT *
               FROM `user`
               WHERE login_name = 'sidneygao');

update coupon set deleted = '1' where amount = 588800 and `product_types` ='EXPERIENCE' and `coupon_type` ='NEWBIE_COUPON';
update coupon set deleted = '1' where  id in (315,316,317,318,319,100033);
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (376,22200,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',6000000,'_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (377,17800,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',5000000,'_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (378,9800,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',3000000,'_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (379,5800,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',600000,'_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (380,4800,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',300000,'_90,_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (381,3800,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',200000,'_90,_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (382,1800,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',900000,'_90,_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
INSERT INTO `coupon` (`id`,`amount`,`rate`,`birthday_benefit`,`multiple`,`start_time`,`end_time`,`deadline`,`used_count`,`total_count`,`issued_count`,`active`,`shared`,`created_by`,`created_time`,`activated_by`,`activated_time`,`updated_by`,`updated_time`,`invest_lower_limit`,`product_types`,`coupon_type`,`user_group`,`sms_alert`,`deleted`,`comment`,`coupon_source`) VALUES (383,800,0,0,0,'2017-01-01 00:00:00','2017-12-31 23:59:59',30,0,9999999,0,1,0,'sidneygao','2017-01-01 19:13:08','sidneygao','2017-01-01 19:13:08',NULL,'2017-01-01 19:13:08',100000,'_30,_90,_180,_360','RED_ENVELOPE','NOT_ACCOUNT_NOT_INVESTED_USER',0,0,'','拓天速贷平台赠送');
COMMIT;