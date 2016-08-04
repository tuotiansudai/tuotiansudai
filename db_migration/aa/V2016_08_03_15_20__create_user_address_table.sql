CREATE TABLE `aa`.`user_address` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`    VARCHAR(50) NOT NULL,
  `real_name`     VARCHAR(50) NOT NULL,
  `mobile`        VARCHAR(18) NOT NULL,
  `address`       VARCHAR(200) NOT NULL,
  `created_by`    VARCHAR(50) NOT NULL,
  `created_time`  DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_USER_ADDRESS_LOGIN_NAME_BY_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  DEFAULT CHARSET = utf8;