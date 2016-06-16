CREATE TABLE `aa`.`extra_loan_rate_rule` (
  `id`                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`                  VARCHAR(12) NOT NULL,
  `product_type`         VARCHAR(10) NOT NULL,
  `level`                INT UNSIGNED NOT NULL,
  `min_invest_amount`   BIGINT UNSIGNED NOT NULL,
  `max_invest_amount`   BIGINT UNSIGNED NOT NULL,
  `rate`                  DOUBLE  NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;