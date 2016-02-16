BEGIN;
INSERT INTO `jpush_alert` (`name`,`push_type`,`push_objects`,`push_source`,`status`,`content`,`jump_to`,`jump_to_link`,`created_time`,`created_by`,`updated_time`,`updated_by`,`is_automatic`) VALUES ('生日提醒','BIRTHDAY_ALERT_DAY',null,'ALL','DISABLED','又长大一岁吧？是不是该多赚点钱，做点大人该做的事啦！生日快乐。','INVEST',NULL ,now(),'admin',NULL,NULL,1);

INSERT INTO `jpush_alert` (`name`,`push_type`,`push_objects`,`push_source`,`status`,`content`,`jump_to`,`jump_to_link`,`created_time`,`created_by`,`updated_time`,`updated_by`,`is_automatic`) VALUES ('放款提醒','LOAN_ALERT',null,'ALL','DISABLED','亲爱的天宝，您投资的{0}元项目已成功放款，恭喜您投资成功。','MY_INVEST',NULL ,now(),'admin',NULL,NULL,1);

INSERT INTO `jpush_alert` (`name`,`push_type`,`push_objects`,`push_source`,`status`,`content`,`jump_to`,`jump_to_link`,`created_time`,`created_by`,`updated_time`,`updated_by`,`is_automatic`) VALUES ('投资提醒','NO_INVEST_ALERT',null,'ALL','DISABLED','亲爱的天宝，一直没见到你。红包都等飞了，火爆活动还不参与？','INVEST',NULL ,now(),'admin',NULL,NULL,1);
COMMIT ;