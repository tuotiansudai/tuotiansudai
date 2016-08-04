CREATE TABLE `aa`.`booking_loan` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile` VARCHAR(18) NOT NULL,
  `source` VARCHAR(15) NOT NULL,
  `booking_time` DATETIME NOT NULL,
  `product_type` VARCHAR(10) NOT NULL,
  `amount` BIGINT(20) UNSIGNED NOT NULL,
  `notice_time` DATETIME NULL,
  `status` TINYINT(1) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_BOOKING_LOAN_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`mobile`) REFERENCES `aa`.`user` (`mobile`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8;