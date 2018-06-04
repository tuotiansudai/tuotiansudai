CREATE TABLE `fudian`.`register` (
  `id`                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_data`         TEXT,
  `merchant_no`          VARCHAR(50),
  `order_no`             VARCHAR(20),
  `order_date`           VARCHAR(8),
  `ext_mark`             VARCHAR(256),
  `return_url`           VARCHAR(256),
  `notify_url`           VARCHAR(256),
  `real_name`            VARCHAR(32),
  `identity_type`        VARCHAR(256),
  `identity_code`        VARCHAR(256),
  `mobile_phone`         VARCHAR(11),
  `return_response_data` TEXT,
  `notify_response_data` TEXT,
  `query_response_data`  TEXT,
  `ret_code`             VARCHAR(4),
  `ret_msg`              VARCHAR(128),
  `request_time`         DATETIME,
  `response_time`        DATETIME,
  `query_time`           DATETIME,
  PRIMARY KEY (`id`),
  KEY `INDEX_REGISTER_ORDER_NO` (`order_no`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;