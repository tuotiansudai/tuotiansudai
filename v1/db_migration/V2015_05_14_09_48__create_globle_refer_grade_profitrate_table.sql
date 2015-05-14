CREATE TABLE `globle_refer_grade_profitrate` (
  `id` varchar(32) NOT NULL,
  `grade` int(3) DEFAULT NULL,
  `profitrate` double DEFAULT NULL,
  `inputdate` date DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;