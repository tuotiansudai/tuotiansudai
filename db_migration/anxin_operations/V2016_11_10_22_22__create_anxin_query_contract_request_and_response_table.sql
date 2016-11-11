CREATE TABLE `anxin_operations`.`anxin_query_contract_request` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `business_id` VARCHAR(32) NULL,
  `batch_no` VARCHAR(32) NULL,
  `tx_time` VARCHAR(14) NULL,
  `created_time` DATETIME NULL,
  PRIMARY KEY (`id`)
  )
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `anxin_operations`.`anxin_query_contract_response` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `business_id` VARCHAR(32) NOT NULL,
  `batch_no` VARCHAR(32) NOT NULL,
  `tx_time` VARCHAR(14),
  `ret_code` VARCHAR(20) NOT NULL,
  `ret_message` VARCHAR(100) NOT NULL,
  `template_id` VARCHAR(10),
  `is_sign` TINYINT(1),
  `contract_no` VARCHAR(30),
  `file_id` VARCHAR(50),
  `code` VARCHAR(20),
  `message` VARCHAR(100),
  `sign_infos` VARCHAR(700),
  `investment_info` VARCHAR(1500),
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
  )
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

