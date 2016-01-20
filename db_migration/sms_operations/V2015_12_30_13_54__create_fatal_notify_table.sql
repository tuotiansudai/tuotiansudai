CREATE TABLE `sms_operations`.`fatal_notify` (
  `id`          INT(32)      NOT NULL AUTO_INCREMENT,
  `mobile`      VARCHAR(11)  NOT NULL,
  `content`     VARCHAR(200) NOT NULL,
  `ext`         VARCHAR(100),
  `stime`       DATETIME,
  `rrid`        VARCHAR(18),
  `send_time`   DATETIME     NOT NULL,
  `result_code` VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  INDEX INDEX_FATAL_NOTIFY_MOBILE (`mobile`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE `job_fatal_notify`;
DROP TABLE `invest_fatal_notify`;