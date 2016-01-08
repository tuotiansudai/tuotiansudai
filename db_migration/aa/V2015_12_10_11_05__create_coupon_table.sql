CREATE TABLE `aa`.`coupon` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`              VARCHAR(32)    NOT NULL,
  `amount`            INT UNSIGNED NOT NULL DEFAULT 0,
  `start_time`        DATETIME,
  `end_time`          DATETIME,
  `used_count`        INT UNSIGNED NOT NULL DEFAULT 0,
  `total_count`       INT UNSIGNED NOT NULL DEFAULT 0,
  `issued_count`      INT UNSIGNED NOT NULL DEFAULT 0,
  `active`            TINYINT(1) NOT NULL,
  `create_user`       VARCHAR(25) NOT NULL,
  `create_time`       DATETIME NOT NULL,
  `active_user`       VARCHAR(25),
  `active_time`       DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_CREATE_USER_REF_USER_LOGIN_NAME FOREIGN KEY (`create_user`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_ACTIVATE_USER_REF_USER_LOGIN_NAME FOREIGN KEY (`active_user`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;