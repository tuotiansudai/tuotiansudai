CREATE TABLE `aa`.`system_recharge` (
  `id` BIGINT UNSIGNED NOT NULL,
  `login_name` varchar(25) NOT NULL,
  `time` datetime NOT NULL,
  `amount` BIGINT UNSIGNED NOT NULL,
  `success_time` datetime DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_SYSTEM_RECHARGE_LOGIN_NAME_REF_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;