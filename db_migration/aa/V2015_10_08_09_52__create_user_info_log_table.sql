
CREATE TABLE `aa`.`user_info_log` (
  `id` BIGINT UNSIGNED NOT NULL,
  `login_name` varchar(32) NOT NULL,
  `operate_time` datetime DEFAULT NULL,
  `ip` varchar(70) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  INDEX `user_info_log_login_name` (`login_name`) USING BTREE,
) ENGINE=InnoDB DEFAULT CHARSET=utf8;