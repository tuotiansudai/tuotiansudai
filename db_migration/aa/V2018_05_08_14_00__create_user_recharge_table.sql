CREATE TABLE `aa`.`user_recharge` (
  `id`              BIGINT UNSIGNED NOT NULL,
  `login_name`      VARCHAR(25)     NOT NULL,
  `amount`          BIGINT UNSIGNED NOT NULL,
  `fee`             BIGINT UNSIGNED NOT NULL,
  `source`          VARCHAR(20)     NOT NULL,
  `pay_type`        VARCHAR(1),
  `bank_order_no`   VARCHAR(20),
  `bank_order_date` VARCHAR(8),
  `status`          VARCHAR(16)     NOT NULL,
  `updated_time`    DATETIME        NOT NULL,
  `created_time`    DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_RECHARGE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  INDEX INDEX_USER_RECHARGE_BANK_ORDER_NO (bank_order_no)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



