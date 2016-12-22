CREATE TABLE `include_question` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `question_id`        BIGINT UNSIGNED NOT NULL,
  `question_title`     VARCHAR(200)     NOT NULL,
  `question_link`      VARCHAR(200)     NOT NULL,
  `tags`               VARCHAR(50),
  `created_time`       DATETIME        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;