CREATE TABLE `aa`.`coupon_repay` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`        VARCHAR(25)     NOT NULL,
  `coupon_id`         BIGINT UNSIGNED NOT NULL,
  `user_coupon_id`    BIGINT UNSIGNED NOT NULL,
  `invest_id`         BIGINT UNSIGNED NOT NULL,
  `period`            INT             NOT NULL,
  `expected_interest` BIGINT UNSIGNED NOT NULL,
  `actual_interest`   BIGINT UNSIGNED NOT NULL,
  `expected_fee`      BIGINT UNSIGNED NOT NULL,
  `actual_fee`        BIGINT UNSIGNED NOT NULL,
  `repay_amount`      BIGINT UNSIGNED NOT NULL,
  `repay_date`        DATETIME        NOT NULL,
  `actual_repay_date` DATETIME,
  `is_transferred`    BOOLEAN         NOT NULL DEFAULT FALSE,
  `status`            VARCHAR(16)     NOT NULL,
  `created_time`      DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_COUPON_REPAY_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_COUPON_REPAY_COUPON_ID_REF_COUPON_ID FOREIGN KEY (`coupon_id`) REFERENCES `aa`.`coupon` (`id`),
  CONSTRAINT FK_COUPON_REPAY_USER_COUPON_ID_REF_USER_COUPON_ID FOREIGN KEY (`user_coupon_id`) REFERENCES `aa`.`user_coupon` (`id`),
  CONSTRAINT FK_COUPON_REPAY_INVEST_ID_REF_USER_COUPON_ID FOREIGN KEY (`invest_id`) REFERENCES `aa`.`invest` (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;