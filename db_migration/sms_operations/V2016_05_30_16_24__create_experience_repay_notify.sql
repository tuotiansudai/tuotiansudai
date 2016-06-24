CREATE TABLE `sms_operations`.`experience_repay_notify` (
  `id`          INT(32)      NOT NULL AUTO_INCREMENT,
  `mobile`      VARCHAR(11)  NOT NULL,
  `content`     VARCHAR(200) NOT NULL,
  `send_time`   DATETIME     NOT NULL,
  `result_code` VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  INDEX INDEX_EXPERIENCE_REPAY_NOTIFY_MOBILE (`mobile`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;