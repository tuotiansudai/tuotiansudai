CREATE TABLE `aa`.`extra_loan_rate` (
  `id`                     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`               BIGINT UNSIGNED NOT NULL,
  `extra_rate_rule_id`  BIGINT UNSIGNED,
  `min_invest_amount`   BIGINT UNSIGNED NOT NULL,
  `max_invest_amount`   BIGINT UNSIGNED NOT NULL,
  `rate`                  DOUBLE  NOT NULL,
  `created_time`         DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_EXTRA_LOAN_RATE_LOAN_ID_REF_LOAN_ID FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`),
  CONSTRAINT FK_EXTRA_RATE_RULE_ID_REF_EXTRA_LOAN_RATE_RULE_ID FOREIGN KEY (`extra_rate_rule_id`) REFERENCES `aa`.`extra_loan_rate_rule` (`id`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;


CREATE TABLE `aa`.`invest_extra_rate` (
  `id`                     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`               BIGINT UNSIGNED NOT NULL,
  `invest_id`             BIGINT UNSIGNED NOT NULL,
  `is_transfer`          TINYINT(1) NOT NULL DEFAULT '0',
  `amount`                 BIGINT UNSIGNED NOT NULL,
  `extra_rate`            DOUBLE  NOT NULL,
  `expected_interest`    BIGINT UNSIGNED NOT NULL,
  `expected_fee`          BIGINT UNSIGNED NOT NULL,
  `actual_interest`      BIGINT UNSIGNED,
  `actual_fee`            BIGINT UNSIGNED,
  `repay_amount`          BIGINT UNSIGNED,
  `repay_date`            DATETIME NOT NULL,
  `actual_repay_date`    DATETIME,
  `created_time`          DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_INVEST_EXTRA_RATE_LOAN_ID_REF_LOAN_ID FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`),
  CONSTRAINT FK_INVEST_EXTRA_RATE_INVEST_ID_REF_INVEST_ID FOREIGN KEY (`invest_id`) REFERENCES `aa`.`invest` (`id`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;