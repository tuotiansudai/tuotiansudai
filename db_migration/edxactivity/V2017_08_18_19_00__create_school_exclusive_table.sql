
CREATE TABLE `school_exclusive` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`                     BIGINT UNSIGNED NOT NULL,
  `login_name`                  VARCHAR(25) NOT NULL,
  `sum_amount`                  BIGINT(20) DEFAULT '0',
  `created_time`                DATETIME DEFAULT NULL,
  `top_three`                   TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
