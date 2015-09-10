CREATE TABLE `aa`.`invest_repay` (
  `id` BIGINT UNSIGNED NOT NULL,
  `corpus` BIGINT UNSIGNED NOT NULL,
  `default_interest` BIGINT UNSIGNED NOT NULL,
  `expect_interest` BIGINT UNSIGNED NOT NULL,
  `actual_interest` BIGINT UNSIGNED NOT NULL,
  `expect_fee` BIGINT UNSIGNED NOT NULL,
  `actual_fee` BIGINT UNSIGNED NOT NULL,
  `invest_id` BIGINT UNSIGNED NOT NULL,
  `period` INT NOT NULL,
  `repay_date` DATETIME NOT NULL,
  `status` VARCHAR (16) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_INVEST_ID_REF_INVEST FOREIGN KEY (`invest_id`) REFERENCES `aa`.`invest` (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 ;