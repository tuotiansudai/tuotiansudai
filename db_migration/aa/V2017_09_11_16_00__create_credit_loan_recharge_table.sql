CREATE TABLE `aa`.`credit_loan_recharge` (
  `id` BIGINT UNSIGNED NOT NULL,
  `account_name` varchar(25) NOT NULL,
  `operator_name` varchar(25) NOT NULL,
  `created_time` datetime NOT NULL,
  `amount` BIGINT UNSIGNED NOT NULL,
  `updated_time` datetime DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_CREDIT_LOAN_RECHARGE_ACCOUNT_NAME_REF_LOGIN_NAME` FOREIGN KEY (`account_name`) REFERENCES `user` (`login_name`),
  CONSTRAINT `FK_CREDIT_LOAN_RECHARGE_OPERATOR_NAME_REF_LOGIN_NAME` FOREIGN KEY (`operator_name`) REFERENCES `user` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;