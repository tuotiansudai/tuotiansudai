CREATE TABLE `iphone7_invest_lottery` (
  `id`             BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `invest_id`      BIGINT(20)          NOT NULL,
  `login_name`     VARCHAR(25)         NOT NULL,
  `lottery_number` VARCHAR(50)         NOT NULL,
  `invest_amount`  BIGINT(20)          NOT NULL,
  `lottery_time`   DATETIME            NOT NULL,
  `status`         VARCHAR(50)         NOT NULL,
  PRIMARY KEY (`id`),
  INDEX INDEX_IPHONE7_INVEST_LOTTERY_LOGIN_NAME(`login_name`),
  INDEX INDEX_IPHONE7_INVEST_LOTTERY_LOTTERY_NUMBER(`lottery_number`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `iphone7_lottery_config` (
  `id`             BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `period`         INT                 NOT NULL,
  `lottery_number` VARCHAR(50)         NOT NULL,
  `effective_time` DATETIME,
  `created_by`     VARCHAR(25)         NOT NULL,
  `created_time`   DATETIME            NOT NULL,
  `audited_by`     VARCHAR(25),
  `audited_time`   DATETIME,
  `status`         VARCHAR(50)         NOT NULL,
  PRIMARY KEY (`id`),
  INDEX INDEX_IPHONE7_LOTTERY_CONFIG_PERIOD(`period`),
  INDEX INDEX_IPHONE7_LOTTERY_CONFIG_LOTTERY_NUMBER(`lottery_number`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

