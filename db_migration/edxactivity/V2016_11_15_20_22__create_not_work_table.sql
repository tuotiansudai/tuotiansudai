BEGIN;

CREATE TABLE `not_work` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`                  VARCHAR(25) NOT NULL,
  `user_name`                   VARCHAR(25)          DEFAULT NULL,
  `mobile`                      VARCHAR(11) NOT NULL,
  `invest_amount`               INT(20)              DEFAULT '0',
  `recommended_register_amount` INT(11)              DEFAULT '0',
  `recommended_identify_amount` INT(11)              DEFAULT '0',
  `recommended_invest_amount`   INT(20)              DEFAULT '0',
  `send_coupon`                 TINYINT(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name_UNIQUE` (`login_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

COMMIT;