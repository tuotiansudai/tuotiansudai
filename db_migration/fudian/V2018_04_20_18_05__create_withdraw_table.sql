CREATE TABLE `fudian`.`withdraw` (
  `id`                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_data`         TEXT,
  `merchant_no`          VARCHAR(50),
  `user_name`            VARCHAR(32),
  `account_no`           VARCHAR(50),
  `amount`               VARCHAR(20),
  `fee`                  VARCHAR(20),
  `order_no`             VARCHAR(20),
  `order_date`           VARCHAR(8),
  `ext_mark`             VARCHAR(256),
  `return_url`           VARCHAR(256),
  `notify_url`           VARCHAR(256),
  `return_response_data` TEXT,
  `notify_response_data` TEXT,
  `ret_code`             VARCHAR(4),
  `ret_msg`              VARCHAR(128),
  `request_time`         DATETIME,
  `response_time`        DATETIME,
  PRIMARY KEY (`id`),
  KEY `INDEX_WITHDRAW_ORDER_NO` (`order_no`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;