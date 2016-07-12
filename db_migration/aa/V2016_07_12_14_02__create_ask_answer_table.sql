CREATE TABLE `aa`.`ask_answer` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`    VARCHAR(25)     NOT NULL,
  `question_id`   BIGINT UNSIGNED NOT NULL,
  `answer`        TEXT,
  `best_answer`   BOOLEAN         NOT NULL DEFAULT FALSE,
  `favorite`      INT UNSIGNED    NOT NULL DEFAULT 0,
  `approved`      BOOLEAN         NOT NULL DEFAULT FALSE,
  `approved_by`   VARCHAR(25),
  `approved_time` DATETIME,
  `created_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_ASK_ANSWER_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_ASK_ANSWER_APPROVED_BY_REF_USER_LOGIN_NAME FOREIGN KEY (`approved_by`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_ASK_ANSWER_QUESTION_ID__REF_ASK_QUESTION_ID FOREIGN KEY (`question_id`) REFERENCES `aa`.`ask_question` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;