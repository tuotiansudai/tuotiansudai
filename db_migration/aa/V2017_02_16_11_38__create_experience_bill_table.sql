CREATE TABLE `experience_bill` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(25) NOT NULL,
  `operation_type` varchar(32) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `business_type` varchar(32) NOT NULL,
  `note` text,
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_EXPERIENCE_BILL_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `user` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;