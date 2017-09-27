CREATE TABLE `ump_operations`.`credit_loan_recharge_notify_request` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`        VARCHAR(64),
  `sign_type`      VARCHAR(16),
  `sign`           VARCHAR(512)    NOT NULL,
  `mer_id`         VARCHAR(16),
  `version`        VARCHAR(6),
  `order_id`       VARCHAR(64)     NOT NULL,
  `mer_date`       VARCHAR(16),
  `trade_no`       VARCHAR(32),
  `mer_check_date` VARCHAR(16),
  `ret_code`       VARCHAR(8)      NOT NULL,
  `ret_msg`        VARCHAR(512),
  `request_time`   DATETIME        NOT NULL,
  `response_time`  DATETIME,
  `request_data`   TEXT            NOT NULL,
  `response_data`  TEXT,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
