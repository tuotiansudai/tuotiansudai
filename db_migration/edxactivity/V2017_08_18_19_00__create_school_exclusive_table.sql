
CREATE TABLE `school_exclusive` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `invest_id`                   BIGINT UNSIGNED NOT NULL,
  `login_name`                  VARCHAR(25) NOT NULL,
  `sum_amount`                  BIGINT(20) DEFAULT '0',
  `created_time`                DATETIME DEFAULT NULL,
  `top_three`                   TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_SCHOOL_EXCLUSIVE_INVEST_ID` (`invest_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
