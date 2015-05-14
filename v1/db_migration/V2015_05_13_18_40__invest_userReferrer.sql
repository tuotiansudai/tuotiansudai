CREATE TABLE `invest_userReferrer` (
  `id` VARCHAR(32) NOT NULL,
  `invest_id` VARCHAR(32) NOT NULL,
  `time` DATETIME NOT NULL,
  `bonus` DOUBLE DEFAULT NULL,
  `referrer_id` VARCHAR(32),
  `status` VARCHAR(19) NOT NULL,
  `role_name` VARCHAR(100) NOT NULL,
  PRIMARY KEY(`id`),
  CONSTRAINT `invest_userReferrer_fk1` FOREIGN KEY (`invest_id`) REFERENCES `invest` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;