CREATE TABLE `aa`.`user_message` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `message_id`   BIGINT UNSIGNED NOT NULL,
  `login_name`   VARCHAR(25)     NOT NULL,
  `title`        VARCHAR(100)    NOT NULL,
  `content`      TEXT            NULL,
  `read`         BOOLEAN         NOT NULL DEFAULT FALSE,
  `read_time`    DATETIME        NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_MESSAGE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_message_ID_REF_MESSAGE_ID FOREIGN KEY (`message_id`) REFERENCES `aa`.`message` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;
