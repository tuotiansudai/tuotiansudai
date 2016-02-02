CREATE TABLE `ump_operations`.`mer_send_sms_pwd_request` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`       VARCHAR(32)     NOT NULL,
  `sign_type`     VARCHAR(8)      NOT NULL,
  `sign`          VARCHAR(256)    NOT NULL,
  `charset`       VARCHAR(16)     NOT NULL,
  `mer_id`        VARCHAR(8)      NOT NULL,
  `version`       VARCHAR(3)      NOT NULL,
  `user_id`       VARCHAR(64)     NOT NULL,
  `order_id`      VARCHAR(64)     NOT NULL,
  `identity_code` VARCHAR(256)    NOT NULL,
  `request_time`  DATETIME        NOT NULL,
  `request_url`   VARCHAR(2048)   NOT NULL,
  `request_data`  TEXT            NOT NULL,
  `status`        VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`mer_send_sms_pwd_response` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`    BIGINT UNSIGNED NOT NULL,
  `sign_type`     VARCHAR(16),
  `sign`          VARCHAR(512)    NOT NULL,
  `mer_id`        VARCHAR(16),
  `version`       VARCHAR(6),
  `ret_code`      VARCHAR(16)     NOT NULL,
  `ret_msg`       VARCHAR(256),
  `response_time` DATETIME        NOT NULL,
  `response_data` TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_MER_SEND_SMS_RESPONSE_REQUEST_ID_REF_MER_SEND_SMS_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES `ump_operations`.`mer_send_sms_pwd_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;