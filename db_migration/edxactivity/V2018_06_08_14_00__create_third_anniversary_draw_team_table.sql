CREATE TABLE `third_anniversary_draw` (
  `login_name`   VARCHAR(25)     NOT NULL,
  `team_name`    VARCHAR(20)     NOT NULL,
  `team_count`   INT(10)         NOT NULL DEFAULT 0,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`login_name`, `team_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


ALTER TABLE `edxactivity`.`we_chat_help_info` ADD COLUMN `login_name` VARCHAR(25) AFTER `openId`;
ALTER TABLE `edxactivity`.`we_chat_help_info` ADD COLUMN `mobile`  VARCHAR(11) AFTER `login_name`;
ALTER TABLE `edxactivity`.`we_chat_help_info` ADD COLUMN `user_name`  VARCHAR(25) AFTER `mobile`;