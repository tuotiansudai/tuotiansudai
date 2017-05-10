CREATE TABLE `mid_summer_invest` (
  `id`                  INT UNSIGNED    NOT NULL  AUTO_INCREMENT,
  `invest_id`           BIGINT UNSIGNED NOT NULL,
  `amount`              BIGINT UNSIGNED NOT NULL,
  `trading_time`        DATETIME        NOT NULL,
  `login_name`          VARCHAR(25)     NOT NULL,
  `referrer_login_name` VARCHAR(25)     NOT NULL,
  `created_time`        DATETIME                  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_MID_SUMMER_INVEST_INVEST_ID` (`invest_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
