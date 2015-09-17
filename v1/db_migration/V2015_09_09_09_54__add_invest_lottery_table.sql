CREATE TABLE `invest_lottery` (
`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
`invest_id` VARCHAR(32) NOT NULL,
`user_id` VARCHAR(32) NOT NULL,
`type` VARCHAR(16) NOT NULL,
`created_time` DATE NOT NULL,
`prize_type` VARCHAR(8) NOT NULL,
`amount` bigint NULL,
`award_time` datetime NULL,
`is_valid` boolean,
`granted_time` datetime NULL,
`is_granted` boolean,

PRIMARY KEY (`id`),
CONSTRAINT FK_INVEST_LOTTERY_USER_ID_REF_USER_ID FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
CONSTRAINT FK_INVEST_LOTTERY_INVEST_ID_REF_INVEST_ID FOREIGN KEY (`invest_id`) REFERENCES `invest` (`id`),
KEY INVEST_LOTTERY_TYPE (`type`)
)
ENGINE=INNODB
AUTO_INCREMENT = 100001
DEFAULT CHARSET=utf8;
