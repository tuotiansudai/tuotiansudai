BEGIN;
CREATE TABLE `aa`.`user_lottery_time` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile` VARCHAR(18) NOT NULL,
  `login_name` VARCHAR(25) NOT NULL,
  `used_count` BIGINT(20),
  `unused_count` BIGINT(20),
  `created_time` DATETIME,
  `updated_time` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_LOTTERY_TIME_MOBILE_REF_USER_MOBILE FOREIGN KEY (`mobile`) REFERENCES `aa`.`user` (`mobile`),
  CONSTRAINT FK_USER_LOTTERY_TIME_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8;
COMMIT;