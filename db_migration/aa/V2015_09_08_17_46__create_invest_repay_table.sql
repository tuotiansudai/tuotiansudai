CREATE TABLE `aa`.`invest_repay` (
  `id`                BIGINT UNSIGNED NOT NULL,
  `invest_id`         BIGINT UNSIGNED NOT NULL,
  `corpus`            BIGINT UNSIGNED NOT NULL,
  `expected_interest` BIGINT UNSIGNED NOT NULL,
  `actual_interest`   BIGINT UNSIGNED,
  `default_interest`  BIGINT UNSIGNED,
  `expected_fee`      BIGINT UNSIGNED NOT NULL,
  `actual_fee`        BIGINT UNSIGNED,
  `period`            INT             NOT NULL,
  `repay_date`        DATETIME        NOT NULL,
  `actual_repay_date` DATETIME,
  `status`            VARCHAR(16)     NOT NULL,
  `created_time`      DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_INVEST_REPAY_INVEST_ID_REF_INVEST_ID FOREIGN KEY (`invest_id`) REFERENCES `aa`.`invest` (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;