CREATE TABLE `aa`.`bank_user_bill` (
  `id`              BIGINT UNSIGNED NOT NULL         AUTO_INCREMENT,
  `business_id`     BIGINT UNSIGNED NOT NULL,
  `login_name`      VARCHAR(25)     NOT NULL,
  `mobile`          VARCHAR(11)     NOT NULL,
  `user_name`       VARCHAR(50)     NOT NULL,
  `role`            VARCHAR(50)     NOT NULL,
  `amount`          BIGINT UNSIGNED NOT NULL,
  `balance`         BIGINT UNSIGNED NOT NULL,
  `bank_order_no`   VARCHAR(20)     NOT NULL,
  `bank_order_date` VARCHAR(8)      NOT NULL,
  `business_type`   VARCHAR(50)     NOT NULL,
  `operation_type`  VARCHAR(50)     NOT NULL,
  `created_time`    DATETIME        NOT NULL         DEFAULT NOW(),
  PRIMARY KEY (`id`),
  CONSTRAINT FK_BANK_USER_BILL_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;