CREATE TABLE `ask`.`answer` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`    VARCHAR(25)     NOT NULL,
  `question_id`   BIGINT UNSIGNED NOT NULL,
  `answer`        TEXT,
  `best_answer`   BOOLEAN         NOT NULL DEFAULT FALSE,
  `favored_by`    TEXT,
  `approved`      BOOLEAN         NOT NULL DEFAULT FALSE,
  `approved_by`   VARCHAR(25),
  `approved_time` DATETIME,
  `created_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  INDEX FK_ASK_ANSWER_LOGIN_NAME(`login_name`),
  INDEX FK_ASK_ANSWER_APPROVED_BY(`approved_by`),
  CONSTRAINT FK_ASK_ANSWER_QUESTION_ID__REF_ASK_QUESTION_ID FOREIGN KEY (`question_id`) REFERENCES `ask`.`question` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;