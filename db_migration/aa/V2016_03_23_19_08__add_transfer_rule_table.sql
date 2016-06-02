CREATE TABLE `aa`.`transfer_rule` (
  `id`                BIGINT UNSIGNED NOT NULL,
  `level_one_fee`     DOUBLE          NOT NULL,
  `level_one_lower`   INT UNSIGNED    NOT NULL,
  `level_one_upper`   INT UNSIGNED    NOT NULL,
  `level_two_fee`     DOUBLE          NOT NULL,
  `level_two_lower`   INT UNSIGNED    NOT NULL,
  `level_two_upper`   INT UNSIGNED    NOT NULL,
  `level_three_fee`   DOUBLE          NOT NULL,
  `level_three_lower` INT UNSIGNED    NOT NULL,
  `level_three_upper` INT UNSIGNED    NOT NULL,
  `discount`          DOUBLE          NOT NULL,
  `days_limit`        INT UNSIGNED    NOT NULL,
  `updated_by`        VARCHAR(25),
  `updated_time`      DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TRANSFER_RULE_UPDATED_BY_REF_USER_LOGIN_NAME` FOREIGN KEY (`updated_by`) REFERENCES `user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

BEGIN;
INSERT INTO `aa`.`transfer_rule` (`id`, `level_one_fee`, `level_one_lower`, `level_one_upper`, `level_two_fee`, `level_two_lower`, `level_two_upper`, `level_three_fee`, `level_three_lower`, `level_three_upper`, `discount`, `days_limit`)
VALUES (1, 0.01, 1, 29, 0.005, 30, 90, 0, 91, 365, 0.005, 5);
COMMIT;