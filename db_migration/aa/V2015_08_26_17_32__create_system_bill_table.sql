CREATE TABLE `aa`.`system_bill`(
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime NOT NULL,
  `type` varchar(8) ,
  `amount` BIGINT UNSIGNED  NOT NULL,
  `detail` varchar(200) ,
  `business_type` VARCHAR(32)     NOT NULL,
  `order_id`       VARCHAR(32),
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 100001
DEFAULT CHARSET=utf8