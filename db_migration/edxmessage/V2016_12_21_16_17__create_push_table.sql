CREATE TABLE `push` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `push_type`    VARCHAR(100)    NOT NULL,
  `push_source`  VARCHAR(100)    NOT NULL,
  `content`      TEXT            NOT NULL,
  `created_time` DATETIME        NOT NULL,
  `created_by`   VARCHAR(25)     NOT NULL,
  `updated_time` DATETIME                 DEFAULT NULL,
  `updated_by`   VARCHAR(25)              DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
