CREATE TABLE `sms_operations`.`loan_raising_complete_notify` (
  `id`          INT(32)      NOT NULL AUTO_INCREMENT,
  `mobile`      VARCHAR(11)  NOT NULL,
  `content`     VARCHAR(200) NOT NULL,
  `send_time`   DATETIME     NOT NULL,
  `result_code` VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;