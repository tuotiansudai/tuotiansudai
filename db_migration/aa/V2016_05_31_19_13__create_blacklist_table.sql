CREATE TABLE `aa`.`blacklist` (
  `id`           BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)         NOT NULL,
  `created_time` DATETIME            NOT NULL,
  `updated_time` DATETIME            NOT NULL,
  `deleted`      TINYINT(1)          NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_KEY_LOGIN_NAME` (`login_name`),
  CONSTRAINT `FK_BLACKLIST_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;