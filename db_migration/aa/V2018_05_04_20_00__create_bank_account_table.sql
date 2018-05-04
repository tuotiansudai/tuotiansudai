CREATE TABLE `aa`.`bank_account` (
  `id`                  INT(32)          NOT NULL AUTO_INCREMENT,
  `login_name`          VARCHAR(50)      NOT NULL,
  `bank_user_name`      VARCHAR(32)      NOT NULL,
  `bank_account_no`     VARCHAR(50)      NOT NULL,
  `bank_order_no`       VARCHAR(20)      NOT NULL,
  `bank_order_date`     DATETIME         NOT NULL,
  `bank_balance`        BIGINT  UNSIGNED NOT NULL DEFAULT 0,
  `bank_freeze`         BIGINT  UNSIGNED NOT NULL DEFAULT 0,
  `authorization`       tinyint(1)       DEFAULT FALSE,
  `auto_invest`         tinyint(1)       DEFAULT FALSE,
  `auto_repay`          tinyint(1)       DEFAULT FALSE,
  `register_time`       DATETIME         NOT NULL,
  `updated_time`        DATETIME         NOT NULL,

  PRIMARY KEY (`id`),
  CONSTRAINT FK_BANK_ACCOUNT_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  index bank_account_user_name (bank_user_name),
  index bank_account_account_no (bank_account_no),
  index bank_account_order_no (bank_order_no)

)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;