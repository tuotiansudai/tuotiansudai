CREATE TABLE `aa`.`user_bill` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`     VARCHAR(25)     NOT NULL,
  `order_id`       BIGINT UNSIGNED,
  `amount`         BIGINT UNSIGNED NOT NULL,
  `balance`        BIGINT UNSIGNED NOT NULL,
  `freeze`         BIGINT UNSIGNED NOT NULL,
  `operation_type` VARCHAR(32)     NOT NULL,
  `business_type`  VARCHAR(32)     NOT NULL,
  `created_time`   DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_BILL_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;