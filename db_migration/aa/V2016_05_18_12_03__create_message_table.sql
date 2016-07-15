CREATE TABLE `aa`.`message` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`          VARCHAR(100)    NOT NULL,
  `template`       TEXT            NULL,
  `type`           VARCHAR(100)    NOT NULL,
  `user_groups`    VARCHAR(100)    NOT NULL,
  `channels`       VARCHAR(100)    NOT NULL,
  `status`         VARCHAR(100)    NOT NULL,
  `read_count`     BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `activated_by`   VARCHAR(25)     NULL,
  `activated_time` DATETIME        NULL,
  `expired_time`   DATETIME        NOT NULL,
  `created_by`     VARCHAR(25)     NOT NULL,
  `created_time`   DATETIME        NOT NULL,
  `updated_by`     VARCHAR(25)     NOT NULL,
  `updated_time`   DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_MESSAGE_ACTIVATED_BY_REF_USER_LOGIN_NAME FOREIGN KEY (`activated_by`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_MESSAGE_CREATED_BY_REF_USER_LOGIN_NAME FOREIGN KEY (`created_by`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_MESSAGE_UPDATED_BY_REF_USER_LOGIN_NAME FOREIGN KEY (`updated_by`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;
