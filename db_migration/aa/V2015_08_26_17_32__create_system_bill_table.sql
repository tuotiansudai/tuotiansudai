CREATE TABLE `aa`.`system_bill`(
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime NOT NULL,
  `bill_type` varchar(8) ,
  `money` BIGINT UNSIGNED  NOT NULL,
  `detail` varchar(200) ,
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 100001
DEFAULT CHARSET=utf8