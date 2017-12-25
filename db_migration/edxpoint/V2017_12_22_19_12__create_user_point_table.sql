CREATE TABLE `user_point` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`   VARCHAR(25)     NOT NULL,
  `point`        BIGINT UNSIGNED NOT NULL,
  `updated_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  KEY `USER_POINT_LOGIN_NAME` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;

# 上线时，需临时给 sdpoint 用户授权允许访问 aa 库
INSERT INTO `user_point` (`login_name`, `point`, `updated_time`)
  SELECT
    `login_name`,
    `point`,
    now()
  FROM `aa`.`account`;

ALTER TABLE `aa`.`account`
  CHANGE `point` `_point` BIGINT UNSIGNED DEFAULT 0 NOT NULL;
