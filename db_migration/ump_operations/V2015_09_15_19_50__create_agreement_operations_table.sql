CREATE TABLE `ump_operations`.`ptp_mer_bind_agreement_request` (
  `id`                       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`                  VARCHAR(32)     NOT NULL,
  `sign_type`                VARCHAR(8)      NOT NULL,
  `sign`                     VARCHAR(256)    NOT NULL,
  `charset`                  VARCHAR(16)     NOT NULL,
  `mer_id`                   VARCHAR(8)      NOT NULL,
  `version`                  VARCHAR(3)      NOT NULL,
  `ret_url`                  VARCHAR(128)    NOT NULL,
  `notify_url`               VARCHAR(128)    NOT NULL,
  `user_id`                  VARCHAR(32)     NOT NULL,
  `user_bind_agreement_list` VARCHAR(16)     NOT NULL,
  `request_time`             DATETIME        NOT NULL,
  `request_url`              VARCHAR(2048)   NOT NULL,
  `request_data`             TEXT            NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `ump_operations`.`mer_bind_agreement_notify_request` (
  `id`                       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`                  VARCHAR(64),
  `sign_type`                VARCHAR(16),
  `sign`                     VARCHAR(512)    NOT NULL,
  `mer_id`                   VARCHAR(16),
  `version`                  VARCHAR(6),
  `user_id`                  VARCHAR(64),
  `user_bind_agreement_list` VARCHAR(64),
  `ret_code`                 VARCHAR(8)      NOT NULL,
  `ret_msg`                  VARCHAR(512),
  `request_time`             DATETIME        NOT NULL,
  `response_time`            DATETIME,
  `request_data`             TEXT            NOT NULL,
  `response_data`            TEXT,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;