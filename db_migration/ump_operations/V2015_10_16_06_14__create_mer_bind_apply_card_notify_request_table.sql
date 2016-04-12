CREATE TABLE `ump_operations`.`mer_bind_apply_card_notify_request` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`       VARCHAR(32)     NOT NULL,
  `sign_type`     VARCHAR(8)      NOT NULL,
  `sign`          VARCHAR(256)    NOT NULL,
  `mer_id`        VARCHAR(8)      NOT NULL,
  `version`       VARCHAR(3)      NOT NULL,
  `user_id`       VARCHAR(64)     NOT NULL,
  `order_id`      VARCHAR(64),
  `mer_date`      VARCHAR(16),
  `ret_code`      VARCHAR(8)      NOT NULL,
  `ret_msg`       VARCHAR(256),
  `request_time`  DATETIME        NOT NULL,
  `response_time` DATETIME,
  `request_data`  TEXT            NOT NULL,
  `response_data` TEXT,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;