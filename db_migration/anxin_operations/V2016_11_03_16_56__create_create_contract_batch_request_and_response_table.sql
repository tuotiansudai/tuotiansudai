CREATE TABLE `anxin_operations`.`create_contract_batch_request` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `business_id` VARCHAR(32) NOT NULL,
  `batch_no` VARCHAR(32) NOT NULL,
  `tx_time` VARCHAR(14) NOT NULL,
  `order_id` VARCHAR(32) NOT NULL,
  `template_id` VARCHAR(10) NOT NULL,
  `is_sign` TINYINT(1) NOT NULL,
  `sign_infos` VARCHAR(700) NOT NULL,
  `investment_info` VARCHAR(1500) NOT NULL,
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


  CREATE TABLE `anxin_operations`.`create_contract_batch_response` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `business_id` VARCHAR(32) NOT NULL,
  `batch_no` VARCHAR(32) NOT NULL,
  `tx_time` VARCHAR(14),
  `order_id` VARCHAR(32),
  `ret_code` VARCHAR(100) NOT NULL,
  `ret_message` VARCHAR(200) NOT NULL,
  `contract_no` VARCHAR(32),
  `template_id` VARCHAR(10),
  `is_sign` TINYINT(1),
  `sign_infos` VARCHAR(700),
  `investment_info` VARCHAR(1500),
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;
