CREATE TABLE `aa`.`feedback` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `content`      VARCHAR(512)    NOT NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_FEEDBACK_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

