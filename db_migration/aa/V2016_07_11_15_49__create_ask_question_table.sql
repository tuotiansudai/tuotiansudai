CREATE TABLE `aa`.`ask_question` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`         VARCHAR(25)     NOT NULL,
  `question`           VARCHAR(30)     NOT NULL,
  `addition`           TEXT,
  `tags`               VARCHAR(50),
  `answers`            INT UNSIGNED    NOT NULL DEFAULT 0,
  `last_answered_time` DATETIME,
  `approved`           BOOLEAN         NOT NULL DEFAULT FALSE,
  `approved_by`        VARCHAR(25),
  `approved_time`      DATETIME,
  `created_time`       DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_ASK_QUESTION_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_ASK_QUESTION_APPROVED_BY_REF_USER_LOGIN_NAME FOREIGN KEY (`approved_by`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;