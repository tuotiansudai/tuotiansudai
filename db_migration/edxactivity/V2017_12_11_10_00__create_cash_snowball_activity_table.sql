
CREATE TABLE `cash_snowball_activity` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `invest_id`                   BIGINT UNSIGNED NOT NULL,
  `login_name`                  VARCHAR(25) NOT NULL,
  `user_name`                   VARCHAR(25) DEFAULT NULL,
  `mobile`                      VARCHAR(11) NOT NULL,
  `annualized_amount`           BIGINT(20) DEFAULT '0',
  `cash_amount`                 BIGINT(20) DEFAULT '0',
  `created_time`                DATETIME DEFAULT NULL,
  `updated_time`                DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_ACTIVITY_INVEST_INVEST_ID` (`invest_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
