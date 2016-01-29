BEGIN;
INSERT INTO `jpush_alert` (`name`,`push_type`,`push_objects`,`push_source`,`status`,`content`,`jump_to`,`jump_to_link`,`created_time`,`created_by`,`updated_time`,`updated_by`,`is_automatic`) VALUES ('生日提醒','BIRTHDAY_ALERT',null,'ALL','ENABLED','又长大一岁吧？是不是该多赚点钱，做点大人该做的事啦！生日快乐。',null,null,now(),'admin',NULL,NULL,1);
COMMIT ;