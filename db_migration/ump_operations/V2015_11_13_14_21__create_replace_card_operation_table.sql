CREATE TABLE `ump_operations`.`ptp_mer_replace_card_request` (
  `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`             VARCHAR(32)     NOT NULL,
  `sign_type`           VARCHAR(8)      NOT NULL,
  `sign`                VARCHAR(256)    NOT NULL,
  `charset`             VARCHAR(16)     NOT NULL,
  `mer_id`              VARCHAR(8)      NOT NULL,
  `version`             VARCHAR(3)      NOT NULL,
  `ret_url`             VARCHAR(128)    NOT NULL,
  `notify_url`          VARCHAR(128),
  `order_id`            VARCHAR(32)     NOT NULL,
  `mer_date`            VARCHAR(8)      NOT NULL,
  `user_id`             VARCHAR(32)     NOT NULL,
  `card_id`             VARCHAR(256)    NOT NULL,
  `account_name`        VARCHAR(256)    NOT NULL,
  `identity_type`       VARCHAR(32)     NOT NULL,
  `identity_code`       VARCHAR(256)    NOT NULL,
  `request_time`        DATETIME        NOT NULL,
  `request_url`         VARCHAR(2048)   NOT NULL,
  `request_data`        TEXT            NOT NULL,
  `status`              VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;