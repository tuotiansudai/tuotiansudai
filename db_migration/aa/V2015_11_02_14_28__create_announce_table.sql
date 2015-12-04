CREATE TABLE `aa`.`announce` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`        VARCHAR(100)    NOT NULL,
  `content`      TEXT,
  `show_on_home` BOOLEAN                  DEFAULT TRUE,
  `created_time` DATETIME        NOT NULL,
  `update_time`  DATETIME        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;