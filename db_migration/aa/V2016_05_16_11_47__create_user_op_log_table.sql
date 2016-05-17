CREATE TABLE `aa`.`user_op_log` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `op_type`      VARCHAR(50)     NOT NULL,
  `ip`           VARCHAR(25)     NOT NULL,
  `device_id`    VARCHAR(25)     NOT NULL,
  `source`       VARCHAR(25)     NOT NULL,
  `created_time` DATETIME        NOT NULL,
  `description`  TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_NAME_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;