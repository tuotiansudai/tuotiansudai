CREATE TABLE `sms_operations`.`invest_notify` (
  `id`          INT(32)      NOT NULL AUTO_INCREMENT,
  `mobile`      VARCHAR(11)  NOT NULL,
  `content`     VARCHAR(200) NOT NULL,
  `ext`         VARCHAR(100),
  `stime`       DATETIME,
  `rrid`        VARCHAR(18),
  `send_time`   DATETIME     NOT NULL,
  `result_code` VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  INDEX INDEX_INVEST_NOTIFY_MOBILE (`mobile`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;