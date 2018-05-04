CREATE TABLE `aa`.`user_bank_card` (
  `id`           BIGINT UNSIGNED NOT NULL,
  `login_name`   VARCHAR(25)     NOT NULL,
  `bank`         VARCHAR(256)    NOT NULL,
  `bank_code`    VARCHAR(32)     NOT NULL,
  `card_number`  VARCHAR(32)     NOT NULL,
  `order_date`   VARCHAR(8)      NOT NULL,
  `order_no`     VARCHAR(20)     NOT NULL,
  `status`       VARCHAR(50)     NOT NULL,
  `created_time` DATETIME        NOT NULL,
  `updated_time` DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_USER_BANK_CARD_LOGIN_NAME_REF_USER_LOGIN_NAME` FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;