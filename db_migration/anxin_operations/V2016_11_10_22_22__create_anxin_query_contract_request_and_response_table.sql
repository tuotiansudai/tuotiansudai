CREATE TABLE `anxin_operations`.`anxin_query_contract_request` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `batch_no` VARCHAR(32) NULL,
  `contract_type` VARCHAR(32) NOT NULL,
  `tx_time` VARCHAR(14) NULL,
  `json_data` VARCHAR(1500) NULL,
  `created_time` DATETIME NULL,
  PRIMARY KEY (`id`)
  )
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `anxin_operations`.`anxin_query_contract_response` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `request_id` BIGINT(20) UNSIGNED NULL,
  `batch_no` VARCHAR(32) NULL,
  `contract_no` VARCHAR(32) NOT NULL,
  `contract_type` VARCHAR(32) NOT NULL,
  `tx_time` VARCHAR(14) NULL,
  `is_sign` VARCHAR(1) NULL,
  `ret_code` VARCHAR(100),
  `ret_message` VARCHAR(200),
  `json_data` VARCHAR(1500) NULL,
  `created_time` DATETIME NULL,
  PRIMARY KEY (`id`)
  )
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

