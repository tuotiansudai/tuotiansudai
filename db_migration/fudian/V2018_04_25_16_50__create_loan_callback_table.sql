CREATE TABLE `fudian`.`loan_callback` (
  `id`                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_data`         TEXT,
  `merchant_no`          VARCHAR(32),
  `loan_tx_no`           VARCHAR(30),
  `order_no`             VARCHAR(40),
  `order_date`           VARCHAR(8),
  `ext_mark`             VARCHAR(512),
  `return_url`           VARCHAR(256),
  `notify_url`           VARCHAR(256),
  `notify_response_data` TEXT,
  `ret_code`             VARCHAR(4),
  `ret_msg`              VARCHAR(128),
  `request_time`         DATETIME,
  `response_time`        DATETIME,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOAN_CALLBACK_ORDER_NO` (`order_no`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


CREATE TABLE `fudian`.`loan_callback_invest` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_callback_id`  BIGINT UNSIGNED NOT NULL,
  `capital`           VARCHAR(20),
  `interest`          VARCHAR(20),
  `interest_fee`      VARCHAR(20),
  `rate_interest`     VARCHAR(20),
  `invest_user_name`  VARCHAR(32),
  `invest_account_no` VARCHAR(50),
  `invest_order_no`   VARCHAR(50),
  `invest_order_date` VARCHAR(8),
  `order_no`          VARCHAR(32),
  `order_date`        VARCHAR(8),
  `ret_code`          VARCHAR(4),
  `ret_msg`           VARCHAR(128),
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_MERCHANT_TRANSFER_INVEST` FOREIGN KEY (`loan_callback_id`) REFERENCES `fudian`.`loan_callback` (`id`),
  KEY `INDEX_LOAN_CALLBACK_INVEST_INVEST_ORDER_NO` (`order_no`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


