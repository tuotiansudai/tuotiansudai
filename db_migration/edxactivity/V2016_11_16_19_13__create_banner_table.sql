CREATE TABLE `banner` (
  `id`               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`             VARCHAR(100)        NOT NULL,
  `web_image_url`    VARCHAR(200)        NOT NULL,
  `app_image_url`    VARCHAR(200)        NOT NULL,
  `url`              VARCHAR(200)        NOT NULL,
  `app_url`          VARCHAR(200)                 DEFAULT NULL,
  `title`            VARCHAR(100)        NOT NULL,
  `content`          VARCHAR(500)        NOT NULL,
  `shared_url`       VARCHAR(200)        NOT NULL,
  `source`           VARCHAR(100)        NOT NULL,
  `authenticated`    TINYINT(1)          NOT NULL,
  `order`            BIGINT(20) UNSIGNED NOT NULL,
  `active`           TINYINT(1)          NOT NULL,
  `created_by`       VARCHAR(25)         NOT NULL,
  `created_time`     DATETIME            NOT NULL,
  `activated_by`     VARCHAR(25)                  DEFAULT NULL,
  `activated_time`   DATETIME                     DEFAULT NULL,
  `deactivated_by`   VARCHAR(25)                  DEFAULT NULL,
  `deactivated_time` DATETIME                     DEFAULT NULL,
  `deleted`          TINYINT(1)          NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8