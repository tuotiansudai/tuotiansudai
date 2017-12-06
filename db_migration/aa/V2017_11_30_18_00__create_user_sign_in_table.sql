BEGIN;

CREATE TABLE `user_sign_in` (
  `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`        VARCHAR(50)     NOT NULL,
  `sign_in_count`     BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `last_sign_in_time` DATETIME        NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


INSERT INTO user_sign_in (login_name, sign_in_count)
  SELECT
    login_name,
    sign_in_count
  FROM user
  WHERE sign_in_count > 0;


UPDATE user set sign_in_count = 0 where sign_in_count is null;

ALTER TABLE user
  CHANGE sign_in_count sign_in_count_bak BIGINT UNSIGNED DEFAULT 0 NOT NULL;

COMMIT;
