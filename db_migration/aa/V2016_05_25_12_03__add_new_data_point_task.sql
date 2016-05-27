INSERT INTO `aa`.`point_task` VALUES (7,'EACH_SUM_INVEST',5000,now(),1,1);
INSERT INTO `aa`.`point_task` VALUES (8,'FIRST_SINGLE_INVEST',10000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (9,'EACH_RECOMMEND',200,now(),1,1);
INSERT INTO `aa`.`point_task` VALUES (10,'EACH_REFERRER_INVEST',1000,now(),1,1);
INSERT INTO `aa`.`point_task` VALUES (11,'FIRST_REFERRER_INVEST',5000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (12,'FIRST_INVEST_180',1000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (13,'FIRST_TURN_ON_NO_PASSWORD_INVEST',1000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (14,'FIRST_TURN_ON_AUTO_INVEST',1000,now(),1,0);
INSERT INTO `aa`.`point_task` VALUES (15,'FIRST_INVEST_360',1000,now(),1,0);

update user_point_task upk set upk.point = (select point from point_task pk where pk.id = upk.point_task_id );
