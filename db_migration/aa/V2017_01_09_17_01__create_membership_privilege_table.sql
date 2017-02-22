BEGIN;
CREATE TABLE `aa`.`membership_privilege` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `privilege`    VARCHAR(30)     NOT NULL,
  `start_time`   DATETIME        NOT NULL,
  `end_time`     DATETIME        NOT NULL,
  `created_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10001
  DEFAULT CHARSET = utf8;
COMMIT;