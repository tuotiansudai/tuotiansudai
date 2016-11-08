CREATE TABLE `anxin_operations`.`anxin_create_account_request` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `tx_time` VARCHAR(14) NOT NULL,
  `person_name` VARCHAR(150) NOT NULL,
  `ident_type_code` VARCHAR(1) NOT NULL,
  `ident_no` VARCHAR(80) NOT NULL,
  `email` VARCHAR(200),
  `mobile_phone` VARCHAR(20),
  `address` VARCHAR(200),
  `authentication_mode` VARCHAR(10) NOT NULL,
  `not_send_pwd` VARCHAR(1) NOT NULL,
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `anxin_operations`.`anxin_create_account_response` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `tx_time` VARCHAR(14),
  `ret_code` VARCHAR(20) NOT NULL,
  `ret_message` VARCHAR(100) NOT NULL,
  `user_id` VARCHAR(32),
  `person_name` VARCHAR(150),
  `ident_type_code` VARCHAR(1),
  `ident_no` VARCHAR(80),
  `email` VARCHAR(200),
  `mobile_phone` VARCHAR(20),
  `address` VARCHAR(200),
  `authentication_mode` VARCHAR(10),
  `not_send_pwd` VARCHAR(1),
  `anxin_sign_email` VARCHAR(200),
  `anxin_sign_mobile_phone` VARCHAR(20),
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

