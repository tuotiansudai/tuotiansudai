CREATE TABLE `sms_operations`.`sms_history` (
  `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile`    VARCHAR(11)     NOT NULL,
  `channel`   VARCHAR(50)     NOT NULL,
  `content`   VARCHAR(200)    NOT NULL,
  `send_time` DATETIME        NOT NULL,
  `success`   BOOLEAN,
  `backup_id` BIGINT UNSIGNED,
  `response`  TEXT,

  PRIMARY KEY (id),
  INDEX INDEX_SMS_HISTORY_MOBILE (`mobile`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;