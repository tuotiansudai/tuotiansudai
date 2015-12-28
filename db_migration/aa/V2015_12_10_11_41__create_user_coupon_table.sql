CREATE TABLE `aa`.`user_coupon` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`        VARCHAR(25)     NOT NULL,
  `coupon_id`         BIGINT UNSIGNED NOT NULL,
  `loan_id`           BIGINT UNSIGNED NULL,
  `created_time`       DATETIME        NOT NULL,
  `used_time`         DATETIME        NULL,
  `expected_interest` BIGINT UNSIGNED NULL,
  `actual_interest`   BIGINT UNSIGNED NULL,
  `default_interest`  BIGINT UNSIGNED NULL,
  `expected_fee`      BIGINT UNSIGNED NULL,
  `actual_fee`        BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_COUPON_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_USER_COUPON_LOAN_ID_REF_LOAN_ID FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`),
  CONSTRAINT FK_USER_COUPON_COUPON_ID_REF_COUPON_ID FOREIGN KEY (`coupon_id`) REFERENCES `aa`.`coupon` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;