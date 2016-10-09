CREATE TABLE `point_bill` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned DEFAULT NULL,
  `login_name` varchar(25) NOT NULL,
  `point` bigint(20) NOT NULL,
  `business_type` varchar(32) NOT NULL,
  `note` text,
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_POINT_BILL_LOGIN_NAME_REF_USER_LOGIN_NAME` (`login_name`),
  KEY `INDEX_POINT_BILL_ORDER_ID` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8