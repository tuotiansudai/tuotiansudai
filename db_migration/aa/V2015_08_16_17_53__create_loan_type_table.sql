CREATE TABLE `loan_type` (
  `id` varchar(64) NOT NULL,
  `interest_point` varchar(200) DEFAULT NULL,
  `interest_type` varchar(200) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `repay_time_period` int(11) DEFAULT NULL,
  `repay_time_unit` varchar(200) DEFAULT NULL,
  `repay_type` varchar(200) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;