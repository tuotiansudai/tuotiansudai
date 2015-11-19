CREATE TABLE `aa`.`bank_card` (
  `id`                  BIGINT UNSIGNED NOT NULL,
  `login_name`          VARCHAR(25)     NOT NULL,
  `bank_number`         VARCHAR(8),
  `card_number`         VARCHAR(50)     NOT NULL,
  `status`              VARCHAR(50)     NOT NULL,
  `is_open_fastPayment` BOOLEAN DEFAULT FALSE,
  `created_time`        DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_BANK_CARD_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;