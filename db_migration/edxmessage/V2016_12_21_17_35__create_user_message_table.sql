CREATE TABLE `user_message` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `message_id`   BIGINT UNSIGNED NOT NULL,
  `login_name`   VARCHAR(25)     NOT NULL,
  `business_id`  BIGINT UNSIGNED NOT NULL,
  `title`        VARCHAR(100)    NOT NULL,
  `content`      TEXT            NULL,
  `read`         BOOLEAN         NOT NULL DEFAULT FALSE,
  `read_time`    DATETIME        NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_MESSAGE_MESSAGE_ID_REF_MESSAGE_ID FOREIGN KEY (`message_id`) REFERENCES `message` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;
