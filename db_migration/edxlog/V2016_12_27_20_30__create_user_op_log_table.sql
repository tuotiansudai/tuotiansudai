CREATE TABLE `user_op_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `login_name` varchar(25) NOT NULL,
  `mobile` varchar(18) NOT NULL,
  `op_type` varchar(50) NOT NULL,
  `ip` varchar(25) NOT NULL,
  `device_id` varchar(50) DEFAULT NULL,
  `source` varchar(25) NOT NULL,
  `created_time` datetime NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8