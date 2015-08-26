CREATE TABLE `push_history` (
  `id`            BIGINT UNSIGNED AUTO_INCREMENT,
  `notification`  TEXT        NOT NULL,
  `request_data`  TEXT        NOT NULL,
  `response_data` TEXT        NOT NULL,
  `created_time`  DATETIME    NOT NULL,
  `operator`      VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_PUSH_HISTORY_REF_USER_ID FOREIGN KEY (`operator`) REFERENCES `user` (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;