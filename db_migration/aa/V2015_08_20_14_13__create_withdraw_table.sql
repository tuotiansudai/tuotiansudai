CREATE TABLE `aa`.`withdraw` (
  `id`              BIGINT UNSIGNED NOT NULL,
  `login_name`      VARCHAR(25)     NOT NULL,
  `amount`          BIGINT UNSIGNED NOT NULL,
  `fee`             BIGINT UNSIGNED NOT NULL,
  `verify_message`  VARCHAR(128),
  `verify_time`     DATETIME,
  `recheck_message` VARCHAR(128),
  `recheck_time`    DATETIME,
  `created_time`    DATETIME        NOT NULL,
  `status`          VARCHAR(16)     NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_WITHDRAW_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;