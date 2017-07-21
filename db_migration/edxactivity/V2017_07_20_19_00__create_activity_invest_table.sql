
CREATE TABLE `invest_newman_tyrant` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `invest_id`                   BIGINT UNSIGNED NOT NULL,
  `login_name`                  VARCHAR(25) NOT NULL,
  `user_name`                   VARCHAR(25) DEFAULT NULL,
  `mobile`                      VARCHAR(11) NOT NULL,
  `invest_amount`               BIGINT(20) DEFAULT '0',
  `activity_name`               VARCHAR(25) NOT NULL,
  `created_time`                DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_INVEST_REWARD_INVEST_ID` (`invest_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
