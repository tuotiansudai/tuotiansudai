CREATE TABLE `wechat_lottery_prize` (
  `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile`       VARCHAR(11)         NOT NULL,
  `login_name`   VARCHAR(50)         NOT NULL,
  `user_name`    VARCHAR(50)                 ,
  `prize`        VARCHAR(50)         NOT NULL,
  `draw_time`    DATETIME,
  PRIMARY KEY (`id`),
  INDEX INDEX_WECHAT_LOTTERY_PRIZE_MOBILE (`mobile`),
  INDEX INDEX_WECHAT_LOTTERY_PRIZE_LOGIN_NAME (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;