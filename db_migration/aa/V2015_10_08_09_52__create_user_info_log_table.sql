
CREATE TABLE `aa`.`user_info_log` (
  `id` varchar(32) NOT NULL,
  `login_name` varchar(32) NOT NULL,
  `operate_time` datetime DEFAULT NULL,
  `ip` varchar(70) DEFAULT NULL,
  `obj_id` varchar(100) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_info_log_id` (`id`) USING BTREE,
  INDEX `user_info_log_login_name` (`login_name`) USING BTREE,
  INDEX `user_info_log_obj_id` (`obj_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;