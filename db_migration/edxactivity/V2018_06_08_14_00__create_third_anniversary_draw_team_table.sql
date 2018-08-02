CREATE TABLE `third_anniversary_draw` (
  `login_name`   VARCHAR(25) NOT NULL,
  `team_name`    VARCHAR(20) NOT NULL,
  `team_count`   INT(10)     NOT NULL DEFAULT 0,
  `created_time` DATETIME    NOT NULL,
  PRIMARY KEY (`login_name`, `team_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `third_anniversary_help` (
  `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25)     NOT NULL,
  `user_name`  VARCHAR(25),
  `mobile`     VARCHAR(11)     NOT NULL,
  `start_time` DATETIME        NOT NULL,
  `end_time`   DATETIME        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `third_anniversary_help_info` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `help_id`      BIGINT UNSIGNED NOT NULL,
  `login_name`   VARCHAR(25)     NOT NULL,
  `user_name`    VARCHAR(25)     NOT NULL,
  `mobile`       VARCHAR(11)     NOT NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`help_id`, `login_name`),
  CONSTRAINT FK_THIRD_ANNIVERSARY_HELP_ID FOREIGN KEY (`help_id`) REFERENCES `third_anniversary_help` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
