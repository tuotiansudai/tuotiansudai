CREATE TABLE `ump_operations`.`transfer_request` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`           VARCHAR(32)     NOT NULL,
  `sign_type`         VARCHAR(8)      NOT NULL,
  `sign`              VARCHAR(256)    NOT NULL,
  `charset`           VARCHAR(16)     NOT NULL,
  `mer_id`            VARCHAR(8)      NOT NULL,
  `version`           VARCHAR(3)      NOT NULL,
  `order_id`          VARCHAR(32)     NOT NULL,
  `mer_date`          VARCHAR(16)     NOT NULL,
  `mer_account_id`    VARCHAR(30),
  `partic_acc_type`   VARCHAR(4)      NOT NULL,
  `trans_action`      VARCHAR(4)      NOT NULL,
  `partic_user_id`    VARCHAR(64)     NOT NULL,
  `partic_account_id` VARCHAR(30),
  `amount`            VARCHAR(26)     NOT NULL,
  `request_time`      DATETIME        NOT NULL,
  `request_url`       VARCHAR(2048)   NOT NULL,
  `request_data`      TEXT            NOT NULL,
  `status`            VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;