CREATE TABLE `user_point_prize` (
  `point_prize_id` bigint(20) unsigned NOT NULL,
  `login_name` varchar(25) NOT NULL,
  `created_time` datetime NOT NULL,
  `reality` tinyint(1) NOT NULL,
  KEY `FK_USER_POINT_PRIZE_POINT_PRIZE_ID_REF_POINT_PRIZE_ID` (`point_prize_id`),
  CONSTRAINT `FK_USER_POINT_PRIZE_POINT_PRIZE_ID_REF_POINT_PRIZE_ID` FOREIGN KEY (`point_prize_id`) REFERENCES `point_prize` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;