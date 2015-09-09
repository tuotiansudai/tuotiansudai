CREATE TABLE `aa`.`invest_referrer_reward` (
  `id` BIGINT UNSIGNED NOT NULL ,
  `invest_id` BIGINT UNSIGNED NOT NULL,
  `time` datetime NOT NULL,
  `bonus` BIGINT UNSIGNED DEFAULT NULL,
  `referrer_login_name` varchar(25) NOT NULL,
  `status` varchar(19) ,
  `role_name` varchar(100) ,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_INVEST_RREFERRER_REWARD_REF_INVEST_INVEST_ID` FOREIGN KEY (`invest_id`) REFERENCES `aa`.`invest` (`id`),
  CONSTRAINT `FK_INVEST_REFERRER_REWARD_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`referrer_login_name`) REFERENCES `aa`.`user` (`login_name`)
) ENGINE=InnoDB
  DEFAULT
  CHARSET=utf8;