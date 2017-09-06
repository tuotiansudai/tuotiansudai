
CREATE TABLE `nationam_mid_autumn` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`                     BIGINT UNSIGNED NOT NULL,
  `login_name`                  VARCHAR(25) NOT NULL,
  `user_name`                   VARCHAR(25) DEFAULT NULL,
  `mobile`                      VARCHAR(11) NOT NULL,
  `loan_type`                   VARCHAR(25) NOT NULL,
  `invest_cash`                 BIGINT(20) DEFAULT '0',
  `invest_coupon`               BIGINT(20) DEFAULT '0',
  `money_amount`                BIGINT(10) DEFAULT '0',
  `created_time`                DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
