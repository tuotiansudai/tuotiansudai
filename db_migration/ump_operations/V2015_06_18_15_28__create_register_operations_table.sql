CREATE TABLE `${ump_operations}`.`mer_register_person_request` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`       VARCHAR(32)     NOT NULL,
  `sign_type`     VARCHAR(8)      NOT NULL,
  `sign`          VARCHAR(256)    NOT NULL,
  `charset`       VARCHAR(16)     NOT NULL,
  `mer_id`        VARCHAR(8)      NOT NULL,
  `version`       VARCHAR(3)      NOT NULL,
  `mer_cust_id`   VARCHAR(32)     NOT NULL,
  `mer_cust_name` VARCHAR(256)    NOT NULL,
  `identity_type` VARCHAR(256)    NOT NULL,
  `identity_code` VARCHAR(256)    NOT NULL,
  `mobile_id`     VARCHAR(11)     NOT NULL,
  `request_time`  DATETIME        NOT NULL,
  `request_url`   VARCHAR(2048)   NOT NULL,
  `request_data`  TEXT            NOT NULL,
  `status`        VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `${ump_operations}`.`mer_register_person_response` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`    BIGINT UNSIGNED NOT NULL,
  `sign_type`     VARCHAR(8)      NOT NULL,
  `sign`          VARCHAR(256)    NOT NULL,
  `mer_id`        VARCHAR(8)      NOT NULL,
  `version`       VARCHAR(3)      NOT NULL,
  `ret_code`      VARCHAR(8)      NOT NULL,
  `ret_msg`       VARCHAR(128)    NOT NULL,
  `user_id`       VARCHAR(32),
  `account_id`    VARCHAR(15),
  `reg_date`      VARCHAR(8),
  `response_time` DATETIME        NOT NULL,
  `response_data` TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FR_MER_REGISTER_RESPONSE_REF_MER_REGISTER_REQUEST FOREIGN KEY (`request_id`) REFERENCES `${ump_operations}`.`mer_register_person_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;