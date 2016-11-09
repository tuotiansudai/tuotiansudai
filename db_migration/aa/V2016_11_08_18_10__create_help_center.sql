CREATE TABLE `help_center` (
  `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title`        VARCHAR(200) NOT NULL,
  `content`      TEXT         NOT NULL,
  `category`     VARCHAR(50)  NOT NULL,
  `hot`          BOOLEAN DEFAULT FALSE
  PRIMARY KEY (`id`),
  CONSTRAINT `INDEX_HELP_CENTER_TITLE` (`title`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;