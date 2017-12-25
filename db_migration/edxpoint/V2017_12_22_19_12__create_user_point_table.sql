CREATE TABLE `user_point` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `point`        BIGINT UNSIGNED NOT NULL,
  `updated_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  KEY `USER_POINT_LOGIN_NAME` (`login_name`),
  UNIQUE (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;

