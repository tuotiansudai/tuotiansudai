CREATE TABLE `ump_operations`.`user_search_request` (
  `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`             VARCHAR(32)     NOT NULL,
  `sign_type`           VARCHAR(8)      NOT NULL,
  `sign`                VARCHAR(256)    NOT NULL,
  `charset`             VARCHAR(16)     NOT NULL,
  `mer_id`              VARCHAR(8)      NOT NULL,
  `version`             VARCHAR(3)      NOT NULL,
  `user_id`             VARCHAR(32)     NOT NULL,
  `is_find_account`     VARCHAR(2)      NOT NULL,
  `is_select_agreement` VARCHAR(1)      NOT NULL,
  `request_time`        DATETIME        NOT NULL,
  `request_url`         VARCHAR(2048)   NOT NULL,
  `request_data`        TEXT            NOT NULL,
  `status`              VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`user_search_response` (
  `id`                       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`               BIGINT UNSIGNED NOT NULL,
  `sign_type`                VARCHAR(16),
  `sign`                     VARCHAR(512)    NOT NULL,
  `mer_id`                   VARCHAR(16),
  `version`                  VARCHAR(6),
  `ret_code`                 VARCHAR(16)     NOT NULL,
  `ret_msg`                  VARCHAR(256),
  `plat_user_id`             VARCHAR(64),
  `account_id`               VARCHAR(30),
  `cust_name`                VARCHAR(64),
  `identity_type`            VARCHAR(64),
  `identity_code`            VARCHAR(512),
  `contact_mobile`           VARCHAR(11),
  `mail_addr`                VARCHAR(256),
  `account_state`            VARCHAR(4),
  `balance`                  VARCHAR(26),
  `card_id`                  VARCHAR(256),
  `gate_id`                  VARCHAR(16),
  `user_bind_agreement_list` TEXT,
  `response_time`            DATETIME        NOT NULL,
  `response_data`            TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_SEARCH_RESPONSE_ID_REF_USER_SEARCH_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES `ump_operations`.`user_search_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`ptp_mer_query_request` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`      VARCHAR(32)     NOT NULL,
  `sign_type`    VARCHAR(8)      NOT NULL,
  `sign`         VARCHAR(256)    NOT NULL,
  `charset`      VARCHAR(16)     NOT NULL,
  `mer_id`       VARCHAR(8)      NOT NULL,
  `version`      VARCHAR(3)      NOT NULL,
  `query_mer_id` VARCHAR(16)     NOT NULL,
  `account_type` VARCHAR(4)      NOT NULL,
  `request_time` DATETIME        NOT NULL,
  `request_url`  VARCHAR(2048)   NOT NULL,
  `request_data` TEXT            NOT NULL,
  `status`       VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`ptp_mer_query_response` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`    BIGINT UNSIGNED NOT NULL,
  `sign_type`     VARCHAR(16),
  `sign`          VARCHAR(512)    NOT NULL,
  `mer_id`        VARCHAR(16),
  `version`       VARCHAR(6),
  `ret_code`      VARCHAR(16)     NOT NULL,
  `ret_msg`       VARCHAR(256),
  `query_mer_id`  VARCHAR(16),
  `balance`       VARCHAR(26),
  `account_type`  VARCHAR(4),
  `account_state` VARCHAR(4),
  `response_time` DATETIME        NOT NULL,
  `response_data` TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_PTP_MER_QUERY_RESPONSE_ID_REF_PTP_MER_QUERY_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES `ump_operations`.`ptp_mer_query_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`project_account_search_request` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`      VARCHAR(32)     NOT NULL,
  `sign_type`    VARCHAR(8)      NOT NULL,
  `sign`         VARCHAR(256)    NOT NULL,
  `charset`      VARCHAR(16)     NOT NULL,
  `mer_id`       VARCHAR(8)      NOT NULL,
  `version`      VARCHAR(3)      NOT NULL,
  `project_id`   VARCHAR(32)     NOT NULL,
  `request_time` DATETIME        NOT NULL,
  `request_url`  VARCHAR(2048)   NOT NULL,
  `request_data` TEXT            NOT NULL,
  `status`       VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`project_account_search_response` (
  `id`                    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`            BIGINT UNSIGNED NOT NULL,
  `sign_type`             VARCHAR(16),
  `sign`                  VARCHAR(512)    NOT NULL,
  `mer_id`                VARCHAR(16),
  `version`               VARCHAR(6),
  `ret_code`              VARCHAR(16)     NOT NULL,
  `ret_msg`               VARCHAR(256),
  `project_id`            VARCHAR(64),
  `project_account_id`    VARCHAR(30),
  `project_account_state` VARCHAR(4),
  `project_state`         VARCHAR(4),
  `balance`               VARCHAR(26),
  `response_time` DATETIME NOT NULL,
  `response_data`         TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_PROJECT_ACCOUNT_SEARCH_RESPONSE_ID_REF_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES `ump_operations`.`project_account_search_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;