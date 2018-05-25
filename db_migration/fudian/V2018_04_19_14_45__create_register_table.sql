CREATE TABLE `fudian`.`register` (
  `id`                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_data`         TEXT,
  `merchant_no`          VARCHAR(32),
  `order_no`             VARCHAR(40),
  `order_date`           VARCHAR(8),
  `ext_mark`             VARCHAR(512),
  `return_url`           VARCHAR(256),
  `notify_url`           VARCHAR(256),
  `real_name`            VARCHAR(32),
  `identity_type`        VARCHAR(32),
  `identity_code`        VARCHAR(18),
  `mobile_phone`         VARCHAR(11),
  `return_response_data` TEXT,
  `notify_response_data` TEXT,
  `ret_code`             VARCHAR(4),
  `ret_msg`              VARCHAR(128),
  `request_time`         DATETIME,
  `response_time`        DATETIME,
  PRIMARY KEY (`id`),
  KEY `INDEX_REGISTER_ORDER_NO` (`order_no`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;