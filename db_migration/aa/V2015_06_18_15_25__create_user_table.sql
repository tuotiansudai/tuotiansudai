CREATE TABLE `aa`.`user` (
  `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`         VARCHAR(25)     NOT NULL,
  `password`           VARCHAR(100)    NOT NULL,
  `email`              VARCHAR(100),
  `mobile`             VARCHAR(18)     NOT NULL,
  `register_time`      DATETIME        NOT NULL,
  `last_modified_time` DATETIME,
  `last_modified_user` VARCHAR(25),
  `avatar`             VARCHAR(256),
  `referrer`           VARCHAR(25),
  `status`             VARCHAR(20)     NOT NULL,
  `salt`               VARCHAR(32)     NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_REFERRER_REF_USER_LOGIN_NAME FOREIGN KEY (`referrer`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_USER_LAST_MODIFIED_USER_REF_USER_LOGIN_NAME FOREIGN KEY (`last_modified_user`) REFERENCES `aa`.`user` (`login_name`),
  UNIQUE KEY UNIQUE_USER_LOGIN_NAME (`login_name`),
  UNIQUE KEY UNIQUE_USER_MOBILE (`mobile`),
  INDEX INDEX_USER_EMAIL (`email`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`account` (
  `id`              INT(32)          NOT NULL AUTO_INCREMENT,
  `login_name`      VARCHAR(50)      NOT NULL,
  `user_name`       VARCHAR(50)      NOT NULL,
  `identity_number` VARCHAR(20)      NOT NULL,
  `pay_user_id`     VARCHAR(32)      NOT NULL,
  `pay_account_id`  VARCHAR(15)      NOT NULL,
  `balance`         BIGINT  UNSIGNED NOT NULL DEFAULT 0,
  `freeze`          BIGINT  UNSIGNED NOT NULL DEFAULT 0,
  `register_time`   DATETIME         NOT NULL,
  `auto_invest`     tinyint(1)        DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_ACCOUNT_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;