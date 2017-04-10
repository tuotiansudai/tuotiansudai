CREATE TABLE `risk_estimate` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `age`          VARCHAR(50)     NOT NULL,
  `income`       VARCHAR(50)     NOT NULL,
  `investment`   VARCHAR(50)     NOT NULL,
  `experience`   VARCHAR(50)     NOT NULL,
  `attitude`     VARCHAR(50)     NOT NULL,
  `duration`     VARCHAR(50)     NOT NULL,
  `loss`         VARCHAR(50)     NOT NULL,
  `rate`         VARCHAR(50)     NOT NULL,
  `estimate`     VARCHAR(50)     NOT NULL,
  `created_time` DATETIME        NOT NULL,
  `updated_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_RISK_ESTIMATE_LOGIN_NAME` (`login_name`),
  CONSTRAINT FK_RISK_ESTIMATE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10001
  DEFAULT CHARSET = utf8;