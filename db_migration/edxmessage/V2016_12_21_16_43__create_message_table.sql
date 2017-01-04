CREATE TABLE `message` (
  `id`               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`            VARCHAR(100)    NOT NULL,
  `template`         TEXT            NULL,
  `template_txt`     TEXT            NULL,
  `type`             VARCHAR(100)    NOT NULL,
  `event_type`       VARCHAR(50),
  `user_group`       VARCHAR(100)    NOT NULL,
  `channels`         VARCHAR(100)    NOT NULL,
  `message_category` VARCHAR(30),
  `web_url`          VARCHAR(200),
  `app_url`          VARCHAR(30),
  `status`           VARCHAR(100)    NOT NULL,
  `read_count`       BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `push_id`          BIGINT UNSIGNED,
  `activated_by`     VARCHAR(25)     NULL,
  `activated_time`   DATETIME        NULL,
  `expired_time`     DATETIME        NOT NULL,
  `created_by`       VARCHAR(25)     NOT NULL,
  `created_time`     DATETIME        NOT NULL,
  `updated_by`       VARCHAR(25)     NOT NULL,
  `updated_time`     DATETIME        NOT NULL,
  `deleted`          BOOLEAN         NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_MESSAGE_PUSH_ID_REF_PUSH_ID FOREIGN KEY (`push_id`) REFERENCES `push` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;