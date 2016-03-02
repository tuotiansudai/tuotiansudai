ALTER TABLE `jpush_alert` MODIFY `created_by` VARCHAR(25) NULL;
BEGIN;
INSERT INTO `jpush_alert` (`name`,`push_type`,`push_objects`,`push_source`,`status`,`content`,`jump_to`,`jump_to_link`,`created_time`,`created_by`,`updated_time`,`updated_by`,`is_automatic`) VALUES ('生日月加息活动提醒','BIRTHDAY_ALERT_MONTH',null,'ALL','DISABLED','生日月快乐,本月投资收益翻倍,最高可达26%,错过还要等一年哟！',null,NULL ,now(),null,NULL,NULL,1);
COMMIT;
