CREATE TABLE `sms_operations`.`membership_give_notify` (
  `id`          INT(32)      NOT NULL AUTO_INCREMENT,
  `mobile`      VARCHAR(11)  NOT NULL,
  `content`     VARCHAR(200) NOT NULL,
  `send_time`   DATETIME     NOT NULL,
  `result_code` VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  INDEX INDEX_TRANSFER_LOAN_NOTIFY_MOBILE (`mobile`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;