CREATE TABLE `anxin_operations`.`anxin_sign_request` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(32) NOT NULL,
  `authorization_time` VARCHAR(14) NOT NULL,
  `location` VARCHAR(100) NOT NULL,
  `sign_location` VARCHAR(200) NOT NULL,
  `project_code` VARCHAR(32) NOT NULL,
  `is_proxy_sign` VARCHAR(1) NULL,
  `is_copy` VARCHAR(1) NULL,
  `created_time` DATETIME NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;



CREATE TABLE `anxin_operations`.`anxin_contract_request` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id` BIGINT UNSIGNED NOT NULL,
  `invest_id` BIGINT UNSIGNED NOT NULL,
  `contract_no` VARCHAR(32),
  `agent_sign_id` BIGINT UNSIGNED NOT NULL,
  `investor_sign_id` BIGINT UNSIGNED NOT NULL,
  `tx_time` VARCHAR(14) NULL,
  `batch_no` VARCHAR(32) NOT NULL,
  `template_id` VARCHAR(10) NOT NULL,
  `is_sign` VARCHAR(1) NULL,
  `agent_mobile` VARCHAR(18) NULL,
  `loaner_identity_number` VARCHAR(20) NULL,
  `recheck_time` VARCHAR(14) NULL,
  `total_rate` VARCHAR(15) NULL,
  `investor_mobile` VARCHAR(18) NULL,
  `agent_identity_number` VARCHAR(20) NULL,
  `periods` VARCHAR(2) NULL,
  `pledge` VARCHAR(45) NULL,
  `end_time` VARCHAR(20) NULL,
  `investor_identity_number` VARCHAR(20) NULL,
  `loaner_user_name` VARCHAR(50) NULL,
  `loan_amount` VARCHAR(15) NULL,
  `created_time` DATETIME NULL,
  `updated_time` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_CONTRACT_REQUEST_AGEN_SIGN_ID_REF_SIGN_REQUEST_ID FOREIGN KEY (`agent_sign_id`) REFERENCES `anxin_operations`.`anxin_sign_request` (`id`),
  CONSTRAINT FK_CONTRACT_REQUEST_INVESTOR_SIGN_ID_REF_IGN_REQUEST_ID FOREIGN KEY (`investor_sign_id`) REFERENCES `anxin_operations`.`anxin_sign_request` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;



  CREATE TABLE `anxin_operations`.`anxin_contract_response` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id` BIGINT UNSIGNED NOT NULL,
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
