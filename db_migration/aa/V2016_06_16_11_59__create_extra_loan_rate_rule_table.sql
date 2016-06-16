CREATE TABLE `aa`.`extra_loan_rate_rule` (
  `name`                  VARCHAR(12) NOT NULL,
  `product_type`         VARCHAR(10) NOT NULL,
  `min_invest_amount`   BIGINT UNSIGNED NOT NULL,
  `max_invest_amount`   BIGINT UNSIGNED NOT NULL,
  `rate`                  DOUBLE  NOT NULL
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;