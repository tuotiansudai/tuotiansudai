CREATE TABLE ${aa}.`recharge` (
  `id`           VARCHAR(32) NOT NULL,
  `login_name`   VARCHAR(25) NOT NULL,
  `amount`       INT(13)     NOT NULL,
  `fee`          INT(13)     NOT NULL,
  `bank`         VARCHAR(8)  NOT NULL,
  `status`       VARCHAR(16) NOT NULL,
  `created_time` DATETIME    NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_RECHARGE_LOGIN_NAME_FOR_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES ${aa}.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;