CREATE TABLE `membership_purchase` (
  `id`           BIGINT UNSIGNED NOT NULL,
  `login_name`   VARCHAR(25)     NOT NULL,
  `mobile`       VARCHAR(11)     NOT NULL,
  `user_name`    VARCHAR(50)     NOT NULL,
  `level`        INT UNSIGNED    NOT NULL,
  `duration`     INT UNSIGNED    NOT NULL,
  `amount`       BIGINT UNSIGNED NOT NULL,
  `source`       VARCHAR(16)     NOT NULL,
  `status`       VARCHAR(16)     NOT NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_MEMBERSHIP_PURCHASE_LOGIN_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`),
  INDEX INDEX_MEMBERSHIP_PURCHASE_STATUS (`status`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;