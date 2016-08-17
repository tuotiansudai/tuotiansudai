CREATE TABLE `ask`.`question` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`         VARCHAR(25)     NOT NULL,
  `question`           VARCHAR(30)     NOT NULL,
  `addition`           TEXT,
  `tags`               VARCHAR(50),
  `answers`            INT UNSIGNED    NOT NULL DEFAULT 0,
  `last_answered_time` DATETIME,
  `approved_by`        VARCHAR(25),
  `approved_time`      DATETIME,
  `rejected_by`        VARCHAR(25),
  `rejected_time`      DATETIME,
  `status`             VARCHAR(20)     NOT NULL,
  `created_time`       DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  INDEX INDEX_ASK_QUESTION_LOGIN_NAME(`login_name`),
  INDEX INDEX_ASK_QUESTION_APPROVED_BY(`approved_by`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;