BEGIN;
CREATE TABLE `htracking_user` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `mobile`        VARCHAR(18) NOT NULL,
    `device_id`     VARCHAR(50) NOT NULL,
    `created_time`  DATETIME,
    PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10001
  DEFAULT CHARSET = utf8;
COMMIT;