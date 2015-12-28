CREATE TABLE `ump_operations`.`transfer_search_request` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`      VARCHAR(32)     NOT NULL,
  `sign_type`    VARCHAR(8)      NOT NULL,
  `sign`         VARCHAR(256)    NOT NULL,
  `charset`      VARCHAR(16)     NOT NULL,
  `mer_id`       VARCHAR(8)      NOT NULL,
  `version`      VARCHAR(3)      NOT NULL,
  `order_id`     VARCHAR(64)     NOT NULL,
  `mer_date`     VARCHAR(16)     NOT NULL,
  `busi_type`    VARCHAR(4)      NOT NULL,
  `request_time` DATETIME        NOT NULL,
  `request_url`  VARCHAR(2048)   NOT NULL,
  `request_data` TEXT            NOT NULL,
  `status`       VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`transfer_search_response` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`     BIGINT UNSIGNED NOT NULL,
  `sign_type`      VARCHAR(16),
  `sign`           VARCHAR(512)    NOT NULL,
  `mer_id`         VARCHAR(16),
  `version`        VARCHAR(6),
  `ret_code`       VARCHAR(16)     NOT NULL,
  `ret_msg`        VARCHAR(256),
  `mer_check_date` VARCHAR(16),
  `mer_date`       VARCHAR(16),
  `trade_no`       VARCHAR(32),
  `busi_type`      VARCHAR(4),
  `amount`         VARCHAR(26),
  `orgi_amt`       VARCHAR(26),
  `com_amt`        VARCHAR(26),
  `com_amt_type`   VARCHAR(6),
  `tran_state`     VARCHAR(4),
  `sms_num`        VARCHAR(4),
  `response_time`  DATETIME        NOT NULL,
  `response_data`  TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_TRANSFER_SEARCH_RESPONSE_ID_REF_TRANSFER_SEARCH_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES `ump_operations`.`transfer_search_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;