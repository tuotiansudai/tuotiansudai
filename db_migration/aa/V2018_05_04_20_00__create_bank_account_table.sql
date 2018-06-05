CREATE TABLE `aa`.`bank_account` (
  `id`                            INT(32)         NOT NULL AUTO_INCREMENT,
  `login_name`                    VARCHAR(50)     NOT NULL,
  `bank_user_name`                VARCHAR(32)     NOT NULL,
  `bank_account_no`               VARCHAR(50)     NOT NULL,
  `bank_account_order_no`         VARCHAR(20)     NOT NULL,
  `bank_account_order_date`       VARCHAR(8)      NOT NULL,
  `balance`                       BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `membership_point`              BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `authorization`                 TINYINT(1)               DEFAULT FALSE,
  `auto_invest`                   TINYINT(1)               DEFAULT FALSE,
  `bank_authorization_order_no`   VARCHAR(20),
  `bank_authorization_order_date` VARCHAR(8),
  `created_time`                  DATETIME        NOT NULL,
  `updated_time`                  DATETIME        NOT NULL,

  PRIMARY KEY (`id`),
  CONSTRAINT FK_BANK_ACCOUNT_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  UNIQUE KEY UNIQUE_BANK_ACCOUNT_LOGIN_NAME (`login_name`),
  INDEX INDEX_BANK_ACCOUNT_USER_NAME (bank_user_name),
  INDEX INDEX_BANK_ACCOUNT_ACCOUNT_NO (bank_account_no)

)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;