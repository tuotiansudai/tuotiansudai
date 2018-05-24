CREATE TABLE `aa`.`bank_recharge` (
  `id`              BIGINT UNSIGNED NOT NULL,
  `login_name`      VARCHAR(25)     NOT NULL,
  `amount`          BIGINT UNSIGNED NOT NULL,
  `fee`             BIGINT UNSIGNED NOT NULL,
  `pay_type`        VARCHAR(20)     NOT NULL,
  `bank_order_no`   VARCHAR(20),
  `bank_order_date` VARCHAR(8),
  `status`          VARCHAR(16)     NOT NULL,
  `source`          VARCHAR(10) DEFAULT 'WEB',
  `channel`         VARCHAR(32),
  `updated_time`    DATETIME        NOT NULL,
  `created_time`    DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_BANK_RECHARGE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  INDEX INDEX_BANK_RECHARGE_BANK_ORDER_NO (bank_order_no)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



