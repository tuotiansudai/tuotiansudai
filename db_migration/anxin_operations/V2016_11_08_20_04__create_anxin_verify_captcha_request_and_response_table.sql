CREATE TABLE `anxin_operations`.`anxin_verify_captcha_request` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `tx_time` VARCHAR(14) NOT NULL,
  `user_id` VARCHAR(32) NOT NULL,
  `project_code` VARCHAR(32) NOT NULL,
  `check_code` VARCHAR(6) NOT NULL,
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `anxin_operations`.`anxin_verify_captcha_response` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `tx_time` VARCHAR(14),
  `ret_code` VARCHAR(20) NOT NULL,
  `ret_message` VARCHAR(100) NOT NULL,
  `user_id` VARCHAR(32),
  `project_code` VARCHAR(32),
  `check_code` VARCHAR(6),
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

