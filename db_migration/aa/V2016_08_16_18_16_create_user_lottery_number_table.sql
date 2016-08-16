BEGIN;
CREATE TABLE `aa`.`user_lottery_number` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile` VARCHAR(18) NOT NULL,
  `login_name` VARCHAR(25) NOT NULL,
  `used_count` BIGINT(20) NULL,
  `unused_count` BIGINT(20) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_LOTTERY_NUMBER_MOBILE_REF_USER_MOBILE FOREIGN KEY (`mobile`) REFERENCES `aa`.`user` (`mobile`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8;
COMMIT;