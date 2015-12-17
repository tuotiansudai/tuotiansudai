CREATE TABLE `aa`.`agent_level_rate` (
  `id` 				BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`	    varchar(25) NOT NULL,
  `level` 		 	INT UNSIGNED NOT NULL,
  `rate` 			double DEFAULT NULL,
  `input_time` 		datetime DEFAULT NULL,
  `update_time` 	datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_AGENT_LEVEL_RATE_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8;