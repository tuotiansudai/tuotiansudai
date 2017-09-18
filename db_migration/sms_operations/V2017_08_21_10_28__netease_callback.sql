CREATE TABLE `sms_operations`.`netease_callback_request` (
  `id`             BIGINT UNSIGNED NOT NULL         AUTO_INCREMENT,
  `sms_history_id` BIGINT UNSIGNED NOT NULL,
  `mobile`         VARCHAR(11)     NOT NULL,
  `sendid`         VARCHAR(50)     NOT NULL,
  `result`         VARCHAR(200),
  `send_time`      DATETIME,
  `report_time`    DATETIME,
  `spliced`        INT,
  `callback_time`  DATETIME,
  `created_time`   DATETIME        NOT NULL,

  PRIMARY KEY (id),
  INDEX INDEX_SMS_HISTORY_MOBILE (`mobile`),
  CONSTRAINT FK_NETEASE_CALLBACK_REQUEST_REF_SMS_HISTORY FOREIGN KEY (`sms_history_id`) REFERENCES `sms_operations`.`sms_history` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;