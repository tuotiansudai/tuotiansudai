CREATE TABLE `ump_operations`.`transeq_search_request` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `service`      VARCHAR(32)     NOT NULL,
  `sign_type`    VARCHAR(8)      NOT NULL,
  `sign`         VARCHAR(256)    NOT NULL,
  `charset`      VARCHAR(16)     NOT NULL,
  `mer_id`       VARCHAR(8)      NOT NULL,
  `version`      VARCHAR(3)      NOT NULL,
  `account_id`   VARCHAR(30)     NOT NULL,
  `account_type` VARCHAR(4)      NOT NULL,
  `start_date`   VARCHAR(16)     NOT NULL,
  `end_date`     VARCHAR(16)     NOT NULL,
  `page_num`     VARCHAR(16)     NOT NULL,
  `request_time` DATETIME        NOT NULL,
  `request_url`  VARCHAR(2048)   NOT NULL,
  `request_data` TEXT            NOT NULL,
  `status`       VARCHAR(10),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `ump_operations`.`transeq_search_response` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id`    BIGINT UNSIGNED NOT NULL,
  `sign_type`     VARCHAR(16),
  `sign`          VARCHAR(512)    NOT NULL,
  `mer_id`        VARCHAR(16),
  `version`       VARCHAR(6),
  `ret_code`      VARCHAR(16)     NOT NULL,
  `ret_msg`       VARCHAR(256),
  `total_num`     VARCHAR(24),
  `trans_detail`  TEXT,
  `response_time` DATETIME        NOT NULL,
  `response_data` TEXT            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_TRANSEQ_SEARCH_RESPONSE_ID_REF_TRANSEQ_SEARCH_REQUEST_ID FOREIGN KEY (`request_id`) REFERENCES `ump_operations`.`transeq_search_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;