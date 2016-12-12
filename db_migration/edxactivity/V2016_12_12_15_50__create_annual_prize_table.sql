
CREATE TABLE `annual_prize` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`                  VARCHAR(25) NOT NULL,
  `user_name`                   VARCHAR(25) DEFAULT NULL,
  `mobile`                      VARCHAR(11) NOT NULL,
  `invest_amount`               INT(20) DEFAULT '0',
  `first_send_coupon`           TINYINT(1)  NOT NULL DEFAULT '0',
  `second_send_coupon`          TINYINT(1)  NOT NULL DEFAULT '0',
  `updated_time`                DATETIME DEFAULT NULL,
  `created_time`                DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_NOT_WORK_LOGIN_NAME` (`login_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
