CREATE TABLE `${ump_operations}`.`mer_recharge_person_request` (
  `id`           INT(32)       NOT NULL AUTO_INCREMENT,
  `service`      VARCHAR(32)   NOT NULL,
  `sign_type`    VARCHAR(8)    NOT NULL,
  `sign`         VARCHAR(256)  NOT NULL,
  `charset`      VARCHAR(16)   NOT NULL,
  `mer_id`       VARCHAR(8)    NOT NULL,
  `version`      VARCHAR(3)    NOT NULL,
  `notify_url`   VARCHAR(128)  NOT NULL,
  `order_id`     VARCHAR(32)   NOT NULL,
  `mer_date`     VARCHAR(8)    NOT NULL,
  `pay_type`     VARCHAR(16)   NOT NULL,
  `user_id`      VARCHAR(32)   NOT NULL,
  `amount`       VARCHAR(13)   NOT NULL,
  `gate_id`      VARCHAR(8)    NOT NULL,
  `request_time` DATETIME      NOT NULL,
  `request_url`  VARCHAR(2048) NOT NULL,
  `request_data` TEXT          NOT NULL,
  `status`       VARCHAR(10),
  PRIMARY KEY (`id`),
  UNIQUE KEY MER_RECHARGE_PERSON_REQUEST_ORDER_ID_UNIQUE (`order_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `${ump_operations}`.`recharge_notify_request` (
  `id`             INT(32)      NOT NULL AUTO_INCREMENT,
  `service`        VARCHAR(32)  NOT NULL,
  `sign_type`      VARCHAR(8)   NOT NULL,
  `sign`           VARCHAR(256) NOT NULL,
  `mer_id`         VARCHAR(8)   NOT NULL,
  `version`        VARCHAR(3)   NOT NULL,
  `order_id`       VARCHAR(32)  NOT NULL,
  `mer_date`       VARCHAR(8)   NOT NULL,
  `trade_no`       VARCHAR(16)  NOT NULL,
  `mer_check_date` VARCHAR(8),
  `balance`        VARCHAR(13),
  `com_amt`        VARCHAR(13),
  `ret_code`       VARCHAR(4),
  `ret_msg`        VARCHAR(128),
  `request_time`   DATETIME     NOT NULL,
  `request_data`   TEXT         NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY MER_RECHARGE_PERSON_REQUEST_ORDER_ID_UNIQUE (`order_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `${ump_operations}`.`recharge_notify_response` (
  `id`            INT(32)      NOT NULL AUTO_INCREMENT,
  `request_id`    INT(32)      NOT NULL,
  `sign_type`     VARCHAR(8)   NOT NULL,
  `mer_id`        VARCHAR(8)   NOT NULL,
  `version`       VARCHAR(3)   NOT NULL,
  `order_id`      VARCHAR(32)  NOT NULL,
  `mer_date`      VARCHAR(8)   NOT NULL,
  `ret_code`      VARCHAR(16)  NOT NULL,
  `response_time` DATETIME     NOT NULL,
  `response_data` TEXT         NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FR_RECHARGE_NOTIFY_RESPONSE_REF_RECHARGE_NOTIFY_REQUEST FOREIGN KEY (`request_id`) REFERENCES ${ump_operations}.`recharge_notify_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;