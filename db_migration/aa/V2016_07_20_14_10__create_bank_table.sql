CREATE TABLE `aa`.`bank` (
  `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`                VARCHAR(50) NOT NULL,
  `bank_code`           VARCHAR(20) NOT NULL,
  `image_url`           VARCHAR(100) NOT NULL,
  `single_amount`       BIGINT UNSIGNED NOT NULL,
  `single_day_amount`   BIGINT UNSIGNED NOT NULL,
  `created_by`          VARCHAR(25) NOT NULL,
  `created_time`        DATETIME NOT NULL,
  `updated_by`          VARCHAR(25),
  `updated_time`        DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_CREATED_BY_LOGIN_NAME FOREIGN KEY (`created_by`) REFERENCES user (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;