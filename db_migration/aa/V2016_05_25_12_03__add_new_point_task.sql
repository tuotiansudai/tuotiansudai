BEGIN;
ALTER TABLE point_task ADD start_using BOOLEAN DEFAULT 0;
ALTER TABLE point_task ADD repeat_status BOOLEAN DEFAULT 0;
ALTER TABLE user_point_task ADD point BIGINT(20) DEFAULT 0;
ALTER TABLE user_point_task ADD task_level BIGINT(20) DEFAULT 0;

INSERT INTO `aa`.`point_task` VALUES (7,'EVERT_SUM_INVEST',5000,now(),1,1);
INSERT INTO `aa`.`point_task` VALUES (8,'FIRST_SINGLE_INVEST',10000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (9,'EVERY_INVITE_FRIEND',200,now(),1,1);
INSERT INTO `aa`.`point_task` VALUES (10,'EVERY_INVITE_INVEST',1000,now(),1,1);
INSERT INTO `aa`.`point_task` VALUES (11,'FIRST_INVITE_FRIEND',5000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (12,'FIRST_INVEST_180D',1000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (13,'FIRST_NO_PASSWORD_INVEST',1000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (14,'FIRST_AUTO_INVEST',1000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (15,'FIRST_INVEST_360D',1000,now(),1,0);

update user_point_task upk set upk.point = (select point from point_task pk where pk.id = upk.point_task_id );
COMMIT;

