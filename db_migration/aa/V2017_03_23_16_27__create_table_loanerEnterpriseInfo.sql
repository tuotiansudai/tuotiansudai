CREATE TABLE `loaner_enterprise_info` (
  `id`                          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `loan_id`                     BIGINT(20) UNSIGNED NOT NULL,
  `company_name`                VARCHAR(50)         NOT NULL,
  `enterprise_type`             VARCHAR(20)         NOT NULL,
  `address`                     VARCHAR(100)        NOT NULL,
  `purpose`                     VARCHAR(200)        NOT NULL,
  `factoring_company_name`      VARCHAR(100)        NULL,
  `factoring_company_desc`      VARCHAR(1000)       NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOANER_ENTERPRISE_INFO_LOAN_ID_REF_LOAN_ID` FOREIGN KEY (`loan_id`) REFERENCES `loan` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;