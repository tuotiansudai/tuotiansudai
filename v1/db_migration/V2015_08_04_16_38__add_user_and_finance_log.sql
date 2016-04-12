BEGIN ;
DROP TABLE IF EXISTS `user_info_log`;
CREATE TABLE `user_info_log` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `operate_time` datetime DEFAULT NULL,
  `ip` varchar(70) DEFAULT NULL,
  `obj_id` varchar(100) DEFAULT NULL,
  `description` text,
  `is_success` boolean,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_info_log_id` (`id`) USING BTREE,
  KEY `user_info_log_user_id` (`user_id`) USING BTREE,
  KEY `user_info_log_obj_id` (`obj_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into menu(id, type, label, url, pid, enable, seq_num, description, expanded, language, target, icon) values('user_info_log_list', 'Management', '用户管理日志', '/admin/user/userInfoLogList.htm', 'system_audit', 1, 1, '', 1, '*', '_self','');

alter table `user_bill` add column(`operator` varchar(32) DEFAULT NULL) ;
alter table `user_bill` add index `user_bill_operator` (`operator`)  USING BTREE;

COMMIT ;