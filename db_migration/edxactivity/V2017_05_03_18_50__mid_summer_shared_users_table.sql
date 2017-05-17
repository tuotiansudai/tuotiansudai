CREATE TABLE `mid_summer_shared_users` (
  `id`           INT UNSIGNED NOT NULL  AUTO_INCREMENT,
  `login_name`   VARCHAR(25)  NOT NULL,
  `created_time` DATETIME               DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_MID_SUMMER_SHARED_USERS_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
