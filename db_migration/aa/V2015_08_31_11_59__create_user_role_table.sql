CREATE TABLE `aa`.`user_role` (
  `login_name`   VARCHAR(25) NOT NULL,
  `role`         VARCHAR(20) NOT NULL,
  `created_time` DATETIME    NOT NULL,
  PRIMARY KEY (`login_name`, `role`),
  CONSTRAINT FK_USER_ROLE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;