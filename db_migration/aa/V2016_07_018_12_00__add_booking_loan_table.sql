CREATE TABLE `aa`.`booking_loan` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` VARCHAR(25) NOT NULL,
  `user_name` VARCHAR(50) NOT NULL,
  `mobile` VARCHAR(18) NOT NULL,
  `source` VARCHAR(15) NOT NULL,
  `booking_time` DATETIME NOT NULL,
  `product_type` VARCHAR(10) NOT NULL,
  `amount` BIGINT(20) UNSIGNED NOT NULL,
  `notice_time` DATETIME NULL,
  `status` VARCHAR(10) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `deleted` VARCHAR(10) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_BOOKING_LOAN_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8;