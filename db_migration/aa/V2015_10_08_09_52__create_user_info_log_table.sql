CREATE TABLE `aa`.`user_info_log` (
  `id`           BIGINT UNSIGNED NOT NULL,
  `login_name`   VARCHAR(32)     NOT NULL,
  `operate_time` DATETIME    DEFAULT NULL,
  `ip`           VARCHAR(70) DEFAULT NULL,
  `description`  TEXT,
  PRIMARY KEY (`id`),
  INDEX `user_info_log_login_name` (`login_name`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;