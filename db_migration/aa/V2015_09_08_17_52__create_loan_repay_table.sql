CREATE TABLE `aa`.`loan_repay` (
  `id`                BIGINT UNSIGNED NOT NULL,
  `loan_id`           BIGINT UNSIGNED NOT NULL,
  `corpus`            BIGINT UNSIGNED NOT NULL,
  `expected_interest` BIGINT UNSIGNED NOT NULL,
  `actual_interest`   BIGINT UNSIGNED,
  `default_interest`  BIGINT UNSIGNED,
  `period`            INT             NOT NULL,
  `repay_date`        DATETIME        NOT NULL,
  `actual_repay_date` DATETIME,
  `status`            VARCHAR(16)     NOT NULL,
  `created_time`      DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOAN_REPAY_LOAN_ID_REF_LOAN_ID FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;

