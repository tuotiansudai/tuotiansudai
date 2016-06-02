ALTER TABLE account ADD COLUMN membership_point BIGINT UNSIGNED DEFAULT 0
AFTER point;

ALTER TABLE invest ADD COLUMN invest_fee_rate double DEFAULT 0.1
AFTER loan_id;

CREATE TABLE `aa`.`membership` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `level`          VARCHAR(10) NOT NULL,
  `experience`     BIGINT UNSIGNED NOT NULL,
  `fee`            double,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`user_membership` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`     VARCHAR(50) NOT NULL,
  `membership_id`  BIGINT UNSIGNED NOT NULL,
  `expired_time`   datetime,
  `created_time`   datetime,
  `type`           varchar(10),
  PRIMARY KEY (`id`),
  CONSTRAINT FK_USER_MEMBERSHIP_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_USER_MEMBERSHIP_MEMBERSHIP_ID_REF_MEMBERSHIP_ID FOREIGN KEY (`membership_id`) REFERENCES `aa`.`membership` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `aa`.`membership_experience_bill` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name`     VARCHAR(50) NOT NULL,
  `experience`     BIGINT UNSIGNED NOT NULL,
  `created_time`   datetime,
  `description`    varchar(100),
  PRIMARY KEY (`id`),
  CONSTRAINT FK_MEMBERSHIP_EXPERIENCE_BILL_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;