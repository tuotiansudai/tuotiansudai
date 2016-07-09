CREATE TABLE IF NOT EXISTS `aa`.`login_log_201607` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `source`     VARCHAR(10)     NOT NULL,
  `ip`         VARCHAR(16),
  `device`     VARCHAR(255),
  `login_time` DATETIME        NOT NULL,
  `success`    BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_LOG_201606_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;