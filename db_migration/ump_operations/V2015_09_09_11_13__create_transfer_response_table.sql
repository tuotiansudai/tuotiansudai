CREATE TABLE `ump_operations`.`transfer_response` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`     BIGINT UNSIGNED NOT NULL,
  `sign_type`      VARCHAR(16),
  `sign`           VARCHAR(256),
  `mer_id`         VARCHAR(16),
  `version`        VARCHAR(6),
  `order_id`       VARCHAR(32),
  `mer_date`       VARCHAR(16),
  `trade_no`       VARCHAR(32),
  `mer_check_date` VARCHAR(16),
  `ret_code`       VARCHAR(8)      NOT NULL,
  `ret_msg`        VARCHAR(128),
  `response_time`  DATETIME        NOT NULL,
  `response_data`  TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_TRANSFER_RESPONSE_REF_TRANSFER_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES `ump_operations`.`transfer_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;