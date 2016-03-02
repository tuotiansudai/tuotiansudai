CREATE TABLE `ump_operations`.`mer_recharge_request` (
  `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`         VARCHAR(32)     NOT NULL,
  `sign_type`       VARCHAR(8)      NOT NULL,
  `sign`            VARCHAR(256)    NOT NULL,
  `charset`         VARCHAR(16)     NOT NULL,
  `mer_id`          VARCHAR(8)      NOT NULL,
  `version`         VARCHAR(3)      NOT NULL,
  `ret_url`         VARCHAR(128)    NOT NULL,
  `notify_url`      VARCHAR(128)    NOT NULL,
  `order_id`        VARCHAR(32)     NOT NULL,
  `mer_date`        VARCHAR(8)      NOT NULL,
  `pay_type`        VARCHAR(16)     NOT NULL,
  `recharge_mer_id` VARCHAR(16)     NOT NULL,
  `account_type`    VARCHAR(4)      NOT NULL,
  `amount`          VARCHAR(13)     NOT NULL,
  `gate_id`         VARCHAR(8)      NOT NULL,
  `request_time`    DATETIME        NOT NULL,
  `request_url`     VARCHAR(2048)   NOT NULL,
  `request_data`    TEXT            NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;