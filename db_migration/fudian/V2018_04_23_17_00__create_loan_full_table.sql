CREATE TABLE `fudian`.`loan_full` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_data`      TEXT,
  `merchant_no`       VARCHAR(32),
  `user_name`         VARCHAR(50),
  `account_no`        VARCHAR(50),
  `loan_fee`          VARCHAR(20),
  `loan_tx_no`        VARCHAR(32),
  `loan_order_date`   VARCHAR(8),
  `loan_order_no`     VARCHAR(20),
  `expect_repay_time` VARCHAR(8),
  `order_no`          VARCHAR(40),
  `order_date`        VARCHAR(8),
  `ext_mark`          VARCHAR(512),
  `return_url`        VARCHAR(256),
  `notify_url`        VARCHAR(256),
  `response_data`     TEXT,
  `ret_code`      VARCHAR(4),
  `ret_msg`       VARCHAR(128),
  `request_time`      DATETIME,
  `response_time`     DATETIME,
  PRIMARY KEY (`id`),
  KEY `INDEX_LOAN_FULL_ORDER_NO` (`order_no`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;