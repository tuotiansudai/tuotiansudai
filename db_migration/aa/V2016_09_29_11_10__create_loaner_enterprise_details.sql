CREATE TABLE `loaner_enterprise_details` (
  `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`         BIGINT(20) UNSIGNED NOT NULL,
  `juristic_person` VARCHAR(20)         NOT NULL,
  `shareholder`     VARCHAR(20)         NOT NULL,
  `address`         VARCHAR(100)        NOT NULL,
  `purpose`         VARCHAR(200)        NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOANER_ENTERPRISE_DETAILS_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE `pledge_enterprise` (
  `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`         BIGINT(20) UNSIGNED NOT NULL,
  `pledge_location` VARCHAR(30)         NOT NULL,
  `estimate_amount` VARCHAR(30)         NOT NULL,
  `guarantee`       VARCHAR(30)         NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_PLEDGE_ENTERPRISE_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)

)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;