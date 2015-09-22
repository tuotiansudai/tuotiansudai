CREATE TABLE `aa`.`referrer_relation` (
  `referrer_login_name` VARCHAR(25) NOT NULL,
  `login_name`          VARCHAR(25) NOT NULL,
  `level`               INT(11)     NOT NULL,
  PRIMARY KEY (`referrer_login_name`, `login_name`),
  CONSTRAINT `FK_REFERRER_RELATION_REFERRER_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`referrer_login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT `FK_REFERRER_RELATION_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;