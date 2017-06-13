CREATE TABLE `celebration_draw_coupon` (
  `id`           INT UNSIGNED NOT NULL  AUTO_INCREMENT,
  `login_name`   VARCHAR(25)  NOT NULL,
  `created_time` DATETIME               DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CELEBRATION_DRAW_COUPON_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
