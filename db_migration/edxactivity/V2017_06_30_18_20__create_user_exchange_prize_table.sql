CREATE TABLE `user_exchange_prize` (
  `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile`       VARCHAR(11)         NOT NULL,
  `login_name`   VARCHAR(50)         NOT NULL,
  `user_name`    VARCHAR(50)                 ,
  `prize`        VARCHAR(50)         NOT NULL,
  `activity_category` VARCHAR(50)    NOT NULL,
  `exchange_time` DATETIME,
  PRIMARY KEY (`id`),
  INDEX INDEX_USER_EXCHANGE_PRIZE_MOBILE (`mobile`),
  INDEX INDEX_USER_EXCHANGE_PRIZE_LOGIN_NAME (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;