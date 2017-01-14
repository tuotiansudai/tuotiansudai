
CREATE TABLE `invest_reward` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`                  VARCHAR(25) NOT NULL,
  `user_name`                   VARCHAR(25) DEFAULT NULL,
  `mobile`                      VARCHAR(11) NOT NULL,
  `invest_amount`               BIGINT(20) DEFAULT '0',
  `reward_grade`				        BIGINT(20) DEFAULT '0',
  `updated_time`                DATETIME DEFAULT NULL,
  `created_time`                DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_INVEST_REWARD_LOGIN_NAME` (`login_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
