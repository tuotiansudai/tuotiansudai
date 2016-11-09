
CREATE TABLE `anxin_operations`.`anxin_contract_request` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `business_id` BIGINT UNSIGNED NOT NULL,
  `order_id` BIGINT UNSIGNED NOT NULL,
  `contract_no` VARCHAR(32),
  `contract_type` VARCHAR(32) NOT NULL,
  `tx_time` VARCHAR(14) NULL,
  `batch_no` VARCHAR(32) NOT NULL,
  `template_id` VARCHAR(10) NOT NULL,
  `is_sign` VARCHAR(1) NULL,
  `json_data` VARCHAR(1500) NULL,
  `created_time` DATETIME NULL,
  `updated_time` DATETIME NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;



  CREATE TABLE `anxin_operations`.`anxin_contract_response` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `business_id` BIGINT UNSIGNED NOT NULL,
  `batch_no` VARCHAR(32) NOT NULL,
  `contract_no` VARCHAR(32),
  `tx_time` VARCHAR(14) ,
  `locale` VARCHAR(15) ,
  `ret_code` VARCHAR(100),
  `ret_message` VARCHAR(200) ,
  `created_time` DATETIME NULL,
  `updated_time` DATETIME NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;
