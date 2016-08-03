CREATE TABLE `aa`.`prepare_user` (
  `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `referrer_mobile` VARCHAR(18)     NOT NULL,
  `mobile`          VARCHAR(18)     NOT NULL,
  `channel`         VARCHAR(32)     NOT NULL,
  `created_time`    DATETIME        NOT NULL,

  PRIMARY KEY (`id`),
  CONSTRAINT FK_REFERRER_MOBILE_REF_USER_MOBILE FOREIGN KEY (`referrer_mobile`) REFERENCES user (`mobile`)

)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;