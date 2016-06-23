CREATE TABLE `aa`.`banner` (
  `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`                VARCHAR(100) NOT NULL,
  `web_image_url`     VARCHAR(500) NOT NULL,
  `app_image_url`      VARCHAR(500) NOT NULL,
  `url`                 VARCHAR(100) NOT NULL,
  `content`             VARCHAR(500) NOT NULL,
  `source`              VARCHAR(100) NOT NULL,
  `authenticated`     TINYINT(1) NOT NULL,
  `order`                BIGINT UNSIGNED NOT NULL,
  `active`              TINYINT(1) NOT NULL,
  `created_by`        VARCHAR(25) NOT NULL,
  `created_time`      DATETIME NOT NULL,
  `activated_by`      VARCHAR(25),
  `activated_time`    DATETIME,
  `deactivated_by`    VARCHAR(25),
  `deactivated_time`  DATETIME,
  `deleted`         TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;