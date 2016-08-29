CREATE TABLE `user_lottery_prize` (
  `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile`       VARCHAR(11)         NOT NULL,
  `login_name`   VARCHAR(25)         NOT NULL,
  `prize`        VARCHAR(50)         NOT NULL,
  `lottery_time` DATETIME,
  PRIMARY KEY (`id`),
  INDEX INDEX_USER_LOTTERY_PRIZE_MOBILE (`mobile`),
  INDEX INDEX_USER_LOTTERY_PRIZE_LOGIN_NAME (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;