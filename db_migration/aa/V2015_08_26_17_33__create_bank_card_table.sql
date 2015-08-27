CREATE TABLE `aa`.`bank_card`(
  `id` BIGINT UNSIGNED NOT NULL,
  `bank_number` varchar(8) ,
  `card_number` varchar(50)  ,
  `created_time` datetime NOt NULL,
  `status` varchar(50) ,
  `login_name` varchar(32) ,
  `is_open_fastPayment` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_BANK_CARD_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 100001
DEFAULT CHARSET=utf8;