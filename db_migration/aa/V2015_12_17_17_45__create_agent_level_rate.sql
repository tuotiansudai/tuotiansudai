CREATE TABLE `aa`.`agent_level_rate` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `level`        INT UNSIGNED    NOT NULL,
  `rate`         DOUBLE                   DEFAULT NULL,
  `created_time` DATETIME                 DEFAULT NULL,
  `updated_time` DATETIME                 DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_AGENT_LEVEL_RATE_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;