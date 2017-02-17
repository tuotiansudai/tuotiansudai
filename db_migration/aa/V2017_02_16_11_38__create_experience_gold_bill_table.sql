CREATE TABLE `experience_gold_bill` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(25) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `experience_type` varchar(32) NOT NULL,
  `note` text,
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_EXPERIENCE_GOLD_BILL_LOGIN_NAME_REF_USER_LOGIN_NAME` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;