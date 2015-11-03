CREATE TABLE `aa`.`auto_invest_plan` (
  `id`                        BIGINT UNSIGNED NOT NULL,
  `login_name`                VARCHAR(25)     NOT NULL,
  `min_invest_amount`         BIGINT UNSIGNED NOT NULL,
  `max_invest_amount`         BIGINT UNSIGNED NOT NULL,
  `retention_amount`          BIGINT UNSIGNED NOT NULL,
  `auto_invest_periods`       INT NOT NULL,
  `enabled`                   BOOLEAN         NOT NULL  DEFAULT FALSE,
  `created_time`              DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_AUTO_INVEST_PLAN_LOGIN_NAME` (`login_name`),
  CONSTRAINT `FK_AUTO_INVEST_PLAN_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;