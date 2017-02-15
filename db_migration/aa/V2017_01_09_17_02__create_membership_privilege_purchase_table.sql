BEGIN;
CREATE TABLE `aa`.`membership_privilege_purchase` (
  `id`                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`           VARCHAR(25)     NOT NULL,
  `mobile`               VARCHAR(18)     NOT NULL,
  `user_name`            VARCHAR(50)     NOT NULL,
  `privilege`            VARCHAR(30)     NOT NULL,
  `privilege_price_type` VARCHAR(10)     NOT NULL,
  `amount`               BIGINT UNSIGNED NOT NULL,
  `source`               VARCHAR(16)     NOT NULL,
  `status`               VARCHAR(30)     NOT NULL,
  `created_time`         DATETIME        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_PRIVILEGE_PURCHASE_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10001
  DEFAULT CHARSET = utf8;
COMMIT;