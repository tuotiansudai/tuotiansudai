CREATE TABLE `user_point_task` (
  `login_name` varchar(25) NOT NULL,
  `point_task_id` int(32) NOT NULL,
  `task_level` bigint(20) unsigned NOT NULL DEFAULT '1',
  `point` bigint(20) unsigned NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`login_name`,`point_task_id`,`task_level`),
  KEY `FK_USER_POINT_TASK_REF_POINT_TASK_ID` (`point_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;