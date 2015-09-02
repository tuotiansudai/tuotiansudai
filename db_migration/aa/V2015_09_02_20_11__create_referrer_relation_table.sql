CREATE TABLE `aa`.`referrer_relation` (
  `referrer_id` varchar(25) NOT NULL,
  `login_name` varchar(25) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`referrer_id`,`login_name`),
  CONSTRAINT `FK_REFERRER_RELATION_REFERRER_ID_REF_USER_LOGIN_NAME` FOREIGN KEY (`referrer_id`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT `FK_REFERRER_RELATION_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;