CREATE TABLE `aa`.`user_coupon` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`        VARCHAR(25)     NOT NULL,
  `coupon_id`         BIGINT UNSIGNED NOT NULL,
  `start_time`        DATETIME,
  `end_time`          DATETIME,
  `status`            VARCHAR(25)     NOT NULL,
  `create_time`       DATETIME        NOT NULL,
  `used_time`         DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_COUPON_ID_REF_COUPON_ID FOREIGN KEY (`coupon_id`) REFERENCES `aa`.`coupon` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;