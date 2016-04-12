CREATE TABLE `user_refer_grade_profitrate` (
  `id` varchar(32) NOT NULL,
  `referrer_id` varchar(32) NOT NULL,
  `referrer_name` varchar(50) NOT NULL,
  `grade` int(3) DEFAULT NULL,
  `profitrate` double DEFAULT NULL,
  `inputdate` date DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ref_grade_pft_user_referid_idx` (`referrer_id`),
  CONSTRAINT `fk_ref_grade_pft_user_referid` FOREIGN KEY (`referrer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
