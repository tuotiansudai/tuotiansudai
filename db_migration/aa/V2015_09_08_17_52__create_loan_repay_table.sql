CREATE TABLE `aa`.`loan_repay` (
  `id` BIGINT UNSIGNED NOT NULL,
  `corpus` BIGINT UNSIGNED NOT NULL,
  `default_interest` BIGINT UNSIGNED NOT NULL,
  `expect_interest` BIGINT UNSIGNED NOT NULL,
  `actual_interest` BIGINT UNSIGNED NOT NULL,
  `period` INT NOT NULL,
  `repay_date` DATETIME NOT NULL,
  `status` VARCHAR (16) NOT NULL,
  `time` DATETIME,
  `loan_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LOAN_ID_REF_LOAN FOREIGN KEY (`loan_id`) REFERENCES `aa`.`loan` (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 ;

