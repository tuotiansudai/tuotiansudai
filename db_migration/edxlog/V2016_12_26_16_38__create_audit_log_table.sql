CREATE TABLE `audit_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `auditor_login_name` varchar(25) DEFAULT NULL,
  `auditor_mobile` varchar(18) DEFAULT NULL,
  `operator_login_name` varchar(25) NOT NULL,
  `operator_mobile` varchar(18) NOT NULL,
  `target_id` varchar(25) DEFAULT NULL,
  `operation_type` varchar(20) NOT NULL,
  `ip` varchar(15) NOT NULL,
  `operation_time` datetime NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

