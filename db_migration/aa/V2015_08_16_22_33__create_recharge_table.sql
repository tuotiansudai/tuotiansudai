CREATE TABLE `aa`.`recharge` (
  `id`           BIGINT UNSIGNED NOT NULL,
  `login_name`   VARCHAR(25)     NOT NULL,
  `amount`       BIGINT UNSIGNED NOT NULL,
  `fee`          BIGINT UNSIGNED NOT NULL,
  `bank`         VARCHAR(8)      NOT NULL,
  `status`       VARCHAR(16)     NOT NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_RECHARGE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;