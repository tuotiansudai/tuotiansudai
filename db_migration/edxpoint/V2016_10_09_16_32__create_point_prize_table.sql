CREATE TABLE `point_prize` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(25) DEFAULT NULL,
  `coupon_id` bigint(20) unsigned DEFAULT NULL,
  `cash` bigint(20) unsigned DEFAULT NULL,
  `probability` bigint(20) unsigned NOT NULL,
  `active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_POINT_PRIZE_COUPON_ID_REF_COUPON_ID` (`coupon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8;