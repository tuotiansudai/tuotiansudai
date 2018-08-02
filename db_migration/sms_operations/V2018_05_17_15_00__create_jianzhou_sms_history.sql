CREATE TABLE `sms_operations`.`jz_sms_history` (
  `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile`    VARCHAR(11)    NOT NULL,
  `content`   VARCHAR(200)    NOT NULL,
  `is_voice`  BOOLEAN         NOT NULL DEFAULT 0,
  `send_time` DATETIME        NOT NULL,
  `response`  VARCHAR(50),

  PRIMARY KEY (id),
  INDEX INDEX_JZ_SMS_HISTORY_MOBILES (`mobile`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;